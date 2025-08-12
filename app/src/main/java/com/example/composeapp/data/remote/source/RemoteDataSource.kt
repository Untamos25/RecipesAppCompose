package com.example.composeapp.data.remote.source

import com.example.composeapp.data.remote.categories.model.CategoryDto
import com.example.composeapp.data.remote.recipes.model.RecipeDto

interface RemoteDataSource {
    fun getCategories(): List<CategoryDto>
    fun getCategoryById(categoryId: Int): CategoryDto?
    fun getRecipesByCategoryId(categoryId: Int): List<RecipeDto>
    fun getRecipeById(recipeId: Int): RecipeDto?
    fun getRecipesByIds(recipeIds: List<Int>): List<RecipeDto>
}
