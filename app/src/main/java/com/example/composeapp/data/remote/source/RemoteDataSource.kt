package com.example.composeapp.data.remote.source

import com.example.composeapp.data.remote.categories.model.CategoryDto
import com.example.composeapp.data.remote.recipes.model.RecipeDto

interface RemoteDataSource {
    suspend fun getCategories(): List<CategoryDto>
    suspend fun getRecipesByCategoryId(categoryId: Int): List<RecipeDto>
    suspend fun getRecipeById(recipeId: Int): RecipeDto?
}
