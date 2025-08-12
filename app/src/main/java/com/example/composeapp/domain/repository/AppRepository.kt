package com.example.composeapp.domain.repository

import com.example.composeapp.domain.categories.model.Category
import com.example.composeapp.domain.categories.model.CategoryWithRecipes
import com.example.composeapp.domain.recipes.model.Recipe
import com.example.composeapp.domain.recipes.model.RecipeWithIngredients
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    fun getCategories(): Flow<List<Category>>
    fun getCategoryWithRecipes(categoryId: Int): Flow<CategoryWithRecipes?>
    fun getRecipeWithIngredients(recipeId: Int): Flow<RecipeWithIngredients?>
    fun getFavoriteRecipes(): Flow<List<Recipe>>

    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean)
    suspend fun refreshData()
}
