package com.example.composeapp.data.repository

import com.example.composeapp.data.local.categories.dao.CategoryDao
import com.example.composeapp.data.local.categories.mapper.toDomain
import com.example.composeapp.data.local.recipes.dao.RecipeDao
import com.example.composeapp.data.local.recipes.mapper.toDomain
import com.example.composeapp.data.remote.categories.mapper.toEntity
import com.example.composeapp.data.remote.recipes.mapper.toEntity
import com.example.composeapp.data.remote.source.RemoteDataSource
import com.example.composeapp.domain.categories.model.Category
import com.example.composeapp.domain.categories.model.CategoryWithRecipes
import com.example.composeapp.domain.recipes.model.Recipe
import com.example.composeapp.domain.recipes.model.RecipeWithIngredients
import com.example.composeapp.domain.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val recipeDao: RecipeDao,
    private val remoteDataSource: RemoteDataSource
) : AppRepository {

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

    override suspend fun syncCategories() = withContext(Dispatchers.IO) {
        val categoriesDtoFromServer = remoteDataSource.getCategories()
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
    }

    override suspend fun syncRecipesForCategory(categoryId: Int) = withContext(Dispatchers.IO) {
        val recipesDtoFromServer = remoteDataSource.getRecipesByCategoryId(categoryId)
        val recipesDtoFromServerIds = recipesDtoFromServer.map { it.id }.toSet()

        val recipesFromDb =
            categoryDao.getCategoryWithRecipes(categoryId).first()?.recipeEntities ?: emptyList()
        val recipesFromDbIds = recipesFromDb.map { it.id }.toSet()
        val favoritesRecipes = recipesFromDb.filter { it.isFavorite }.map { it.id }.toSet()

        val recipesIdsToDeleteFromDb = recipesFromDbIds - recipesDtoFromServerIds
        if (recipesIdsToDeleteFromDb.isNotEmpty()) {
            recipeDao.deleteRecipesByIds(recipesIdsToDeleteFromDb.toList())
        }

        val recipeEntitiesWithCurrentFavoriteStatus = recipesDtoFromServer.map { recipeDto ->
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
    }

    override suspend fun syncRecipeDetails(recipeId: Int) = withContext(Dispatchers.IO) {
        try {
            val recipeDtoFormServer = remoteDataSource.getRecipeById(recipeId) ?: return@withContext
            val recipeEntityFromDb =
                recipeDao.getRecipeWithIngredients(recipeId).first()?.recipeEntity
                    ?: return@withContext
            val recipeEntityFromServer =
                recipeDtoFormServer.toEntity(categoryId = recipeEntityFromDb.categoryId)

            val updatedRecipeEntity = recipeEntityFromServer.copy(
                isFavorite = recipeEntityFromDb.isFavorite,
                lastSyncTime = System.currentTimeMillis()
            )

            recipeDao.upsertRecipes(listOf(updatedRecipeEntity))

            val ingredientEntities =
                recipeDtoFormServer.ingredients.mapIndexed { index, ingredientDto ->
                    ingredientDto.toEntity(recipeId = recipeDtoFormServer.id, index = index)
                }
            recipeDao.replaceIngredientsForRecipe(recipeId, ingredientEntities)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
