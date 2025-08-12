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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
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


    override suspend fun refreshData() = withContext(Dispatchers.IO) {
        val favoriteRecipeIds = recipeDao.getFavoriteRecipes().first().map { it.id }.toSet()
        val categoriesDto = remoteDataSource.getCategories()
        val semaphore = Semaphore(5)

        val recipesByCategories = categoriesDto.map { categoryDto ->
            async {
                semaphore.withPermit {
                    categoryDto to remoteDataSource.getRecipesByCategoryId(categoryDto.id)
                }
            }
        }.awaitAll()

        val allRecipes = recipesByCategories.flatMap { (categoryDto, recipesDtoList) ->
            recipesDtoList.map { recipeDto ->
                recipeDto.toEntity(categoryId = categoryDto.id)
                    .copy(isFavorite = favoriteRecipeIds.contains(recipeDto.id))
            }
        }

        val allIngredients = recipesByCategories.flatMap { (_, recipesDtoList) ->
            recipesDtoList.flatMap { recipeDto ->
                recipeDto.ingredients.map { ingredientDto ->
                    ingredientDto.toEntity(recipeId = recipeDto.id)
                }
            }
        }

        val allCategories = categoriesDto.map { it.toEntity() }

        categoryDao.updateAllData(
            recipeDao = recipeDao,
            categories = allCategories,
            recipes = allRecipes,
            ingredients = allIngredients
        )
    }


//    override suspend fun refreshData() {
//        withContext(Dispatchers.IO) {
//            val favoriteRecipeIds = recipeDao.getFavoriteRecipes().first().map { it.id }.toSet()
//
//            val categoriesDto = remoteDataSource.getCategories()
//
//            val allRecipes = mutableListOf<RecipeEntity>()
//            val allIngredients = mutableListOf<IngredientEntity>()
//
//            categoriesDto.forEach { categoryDto ->
//                val recipesDto = remoteDataSource.getRecipesByCategoryId(categoryDto.id)
//
//                allRecipes += recipesDto.map { recipeDto ->
//                    recipeDto.toEntity(categoryId = categoryDto.id)
//                        .copy(isFavorite = favoriteRecipeIds.contains(recipeDto.id))
//                }
//
//                allIngredients += recipesDto.flatMap { recipeDto ->
//                    recipeDto.ingredients.map { ingredientDto ->
//                        ingredientDto.toEntity(recipeId = recipeDto.id)
//                    }
//                }
//            }
//
//            val allCategories = categoriesDto.map { it.toEntity() }
//
//            categoryDao.updateAllData(
//                recipeDao = recipeDao,
//                categories = allCategories,
//                recipes = allRecipes,
//                ingredients = allIngredients
//            )
//        }
//    }


}
