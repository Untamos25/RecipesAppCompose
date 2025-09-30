package com.pavlushinsa.recipescompapp.data.remote.source

import com.pavlushinsa.recipescompapp.data.remote.categories.model.CategoryDto
import com.pavlushinsa.recipescompapp.data.remote.recipes.model.RecipeDto

interface RemoteDataSource {
    suspend fun getCategories(): List<CategoryDto>
    suspend fun getRecipesByCategoryId(categoryId: Int): List<RecipeDto>
    suspend fun getRecipeById(recipeId: Int): RecipeDto?
}
