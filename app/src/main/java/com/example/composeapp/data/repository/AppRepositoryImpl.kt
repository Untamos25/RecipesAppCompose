package com.example.composeapp.data.repository

import android.util.Log
import com.example.composeapp.data.local.categories.dao.CategoryDao
import com.example.composeapp.data.local.categories.mapper.toDomain
import com.example.composeapp.data.local.recipes.dao.RecipeDao
import com.example.composeapp.data.local.recipes.mapper.toDomain
import com.example.composeapp.data.remote.categories.mapper.toEntity
import com.example.composeapp.data.remote.recipes.mapper.toEntity
import com.example.composeapp.data.remote.source.RemoteDataSource
import com.example.composeapp.domain.categories.model.Category
import com.example.composeapp.domain.categories.model.CategoryWithRecipes
import com.example.composeapp.domain.common.DataResult
import com.example.composeapp.domain.common.Error
import com.example.composeapp.domain.recipes.model.Recipe
import com.example.composeapp.domain.recipes.model.RecipeWithIngredients
import com.example.composeapp.domain.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val recipeDao: RecipeDao,
    private val remoteDataSource: RemoteDataSource
) : AppRepository {

    companion object {
        private const val LOG_TAG = "AppRepositoryImpl"
    }

    private object HttpCode {
        const val UNAUTHORIZED = 401
        const val NOT_FOUND = 404
        const val SERVER_ERROR_START = 500
        const val SERVER_ERROR_END = 599
    }

    override fun getCategories(): Flow<List<Category>> {
        return categoryDao.getCategories().map { categoryEntities ->
            categoryEntities.map { it.toDomain() }
        }
    }

    override fun getCategoryWithRecipes(categoryId: Int): Flow<CategoryWithRecipes?> {
        return categoryDao.getCategoryWithRecipes(categoryId).map { it?.toDomain() }
    }

    override fun getRecipeWithIngredients(recipeId: Int): Flow<RecipeWithIngredients?> {
        return recipeDao.getRecipeWithIngredients(recipeId).map { it?.toDomain() }
    }

    override fun getFavoriteRecipes(): Flow<List<Recipe>> {
        return recipeDao.getFavoriteRecipes().map { recipeEntities ->
            recipeEntities.map { it.toDomain() }
        }
    }

    override suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean) {
        recipeDao.updateFavoriteStatus(recipeId, isFavorite)
    }

    override suspend fun syncCategories(): DataResult<Unit, Error> {
        return when (val apiResult = safeApiCall { remoteDataSource.getCategories() }) {
            is DataResult.Success -> withContext(Dispatchers.IO) {
                safeDbCall {
                    val categoriesDtoFromServer = apiResult.value
                    val categoriesDtoFromServerIds = categoriesDtoFromServer.map { it.id }.toSet()
                    val categoriesEntitiesFromServer = categoriesDtoFromServer.map { categoryDto ->
                        categoryDto.toEntity().copy(lastSyncTime = System.currentTimeMillis())
                    }

                    val categoriesFromDb = categoryDao.getCategories().first()
                    val categoriesFromDbIds = categoriesFromDb.map { it.id }.toSet()

                    val categoriesToDelete = categoriesFromDbIds - categoriesDtoFromServerIds
                    if (categoriesToDelete.isNotEmpty()) {
                        categoryDao.deleteCategoriesByIds(categoriesToDelete.toList())
                    }

                    val categoriesToUpdate =
                        categoriesEntitiesFromServer.filter { it.id in categoriesFromDbIds }
                    if (categoriesToUpdate.isNotEmpty()) {
                        categoryDao.updateCategories(categoriesToUpdate)
                    }

                    val categoriesToInsert =
                        categoriesEntitiesFromServer.filter { it.id !in categoriesFromDbIds }
                    if (categoriesToInsert.isNotEmpty()) {
                        categoryDao.insertCategories(categoriesToInsert)
                    }
                }.let {
                    if (it is DataResult.Success) DataResult.Success(Unit)
                    else it as DataResult.Failure
                }
            }

            is DataResult.Failure -> apiResult
        }
    }

    override suspend fun syncRecipesForCategory(categoryId: Int): DataResult<Unit, Error> {
        return when (val apiResult =
            safeApiCall { remoteDataSource.getRecipesByCategoryId(categoryId) }) {
            is DataResult.Success -> withContext(Dispatchers.IO) {
                safeDbCall {
                    val recipesDtoFromServer = apiResult.value
                    val recipesDtoFromServerIds = recipesDtoFromServer.map { it.id }.toSet()

                    val recipesFromDb =
                        categoryDao.getCategoryWithRecipes(categoryId).first()?.recipeEntities
                            ?: emptyList()
                    val recipesFromDbIds = recipesFromDb.map { it.id }.toSet()
                    val favoritesRecipes =
                        recipesFromDb.filter { it.isFavorite }.map { it.id }.toSet()

                    val recipesIdsToDeleteFromDb = recipesFromDbIds - recipesDtoFromServerIds
                    if (recipesIdsToDeleteFromDb.isNotEmpty()) {
                        recipeDao.deleteRecipesByIds(recipesIdsToDeleteFromDb.toList())
                    }

                    val recipeEntitiesWithCurrentFavoriteStatus =
                        recipesDtoFromServer.map { recipeDto ->
                            val isFavorite = favoritesRecipes.contains(recipeDto.id)
                            recipeDto.toEntity(categoryId = categoryId).copy(
                                isFavorite = isFavorite,
                                lastSyncTime = System.currentTimeMillis()
                            )
                        }

                    val allIngredientEntities = recipesDtoFromServer.flatMap { recipeDto ->
                        recipeDto.ingredients.mapIndexed { index, ingredientDto ->
                            ingredientDto.toEntity(recipeId = recipeDto.id, index = index)
                        }
                    }

                    recipeDao.replaceRecipesAndIngredients(
                        recipeEntitiesWithCurrentFavoriteStatus,
                        allIngredientEntities
                    )

                    categoryDao.updateCategorySyncTime(categoryId, System.currentTimeMillis())
                }.let {
                    if (it is DataResult.Success) DataResult.Success(Unit)
                    else it as DataResult.Failure
                }
            }

            is DataResult.Failure -> apiResult
        }
    }

    override suspend fun syncRecipeDetails(recipeId: Int): DataResult<Unit, Error> {
        return when (val apiResult = safeApiCall { remoteDataSource.getRecipeById(recipeId) }) {
            is DataResult.Success -> {
                val recipeDtoFromServer = apiResult.value
                if (recipeDtoFromServer == null) {
                    Log.w(
                        LOG_TAG,
                        "syncRecipeDetails failed: Recipe with id $recipeId not found on server."
                    )
                    return DataResult.Failure(Error.NotFound)
                }

                withContext(Dispatchers.IO) {
                    safeDbCall {
                        val recipeEntityFromDb =
                            recipeDao.getRecipeWithIngredients(recipeId).first()?.recipeEntity

                        if (recipeEntityFromDb == null) return@safeDbCall

                        val recipeEntityFromServer =
                            recipeDtoFromServer.toEntity(categoryId = recipeEntityFromDb.categoryId)

                        val updatedRecipeEntity = recipeEntityFromServer.copy(
                            isFavorite = recipeEntityFromDb.isFavorite,
                            lastSyncTime = System.currentTimeMillis()
                        )

                        recipeDao.upsertRecipes(listOf(updatedRecipeEntity))

                        val ingredientEntities =
                            recipeDtoFromServer.ingredients.mapIndexed { index, ingredientDto ->
                                ingredientDto.toEntity(
                                    recipeId = recipeDtoFromServer.id,
                                    index = index
                                )
                            }
                        recipeDao.replaceIngredientsForRecipe(recipeId, ingredientEntities)
                    }.let {
                        if (it is DataResult.Success) DataResult.Success(Unit)
                        else it as DataResult.Failure
                    }
                }
            }

            is DataResult.Failure -> apiResult
        }
    }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): DataResult<T, Error> {
        return try {
            DataResult.Success(apiCall())
        } catch (e: HttpException) {
            Log.e(LOG_TAG, "HttpException: ${e.message()}", e)
            val error = when (e.code()) {
                HttpCode.UNAUTHORIZED -> Error.Unauthorized
                HttpCode.NOT_FOUND -> Error.NotFound
                in HttpCode.SERVER_ERROR_START..HttpCode.SERVER_ERROR_END -> Error.ServerError
                else -> Error.UnknownError
            }
            DataResult.Failure(error)
        } catch (e: IOException) {
            Log.e(LOG_TAG, "IOException: ${e.message}", e)
            DataResult.Failure(Error.NoInternetConnection)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Unexpected exception: ${e.message}", e)
            DataResult.Failure(Error.UnknownError)
        }
    }

    private suspend fun <T> safeDbCall(dbCall: suspend () -> T): DataResult<T, Error> {
        return try {
            DataResult.Success(dbCall())
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Database exception: ${e.message}", e)
            DataResult.Failure(Error.DatabaseError)
        }
    }
}
