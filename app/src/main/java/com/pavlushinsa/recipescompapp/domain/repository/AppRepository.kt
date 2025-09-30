package com.pavlushinsa.recipescompapp.domain.repository

import com.pavlushinsa.recipescompapp.domain.categories.model.Category
import com.pavlushinsa.recipescompapp.domain.categories.model.CategoryWithRecipes
import com.pavlushinsa.recipescompapp.domain.common.Error
import com.pavlushinsa.recipescompapp.domain.common.DataResult
import com.pavlushinsa.recipescompapp.domain.recipes.model.Recipe
import com.pavlushinsa.recipescompapp.domain.recipes.model.RecipeWithIngredients
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    fun getCategories(): Flow<List<Category>>
    fun getCategoryWithRecipes(categoryId: Int): Flow<CategoryWithRecipes?>
    fun getRecipeWithIngredients(recipeId: Int): Flow<RecipeWithIngredients?>
    fun getFavoriteRecipes(): Flow<List<Recipe>>

    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean)

    suspend fun syncCategories(): DataResult<Unit, Error>
    suspend fun syncRecipesForCategory(categoryId: Int): DataResult<Unit, Error>
    suspend fun syncRecipeDetails(recipeId: Int): DataResult<Unit, Error>
}
