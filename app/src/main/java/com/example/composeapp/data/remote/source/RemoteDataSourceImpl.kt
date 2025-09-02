package com.example.composeapp.data.remote.source

import com.example.composeapp.data.remote.api.RecipesApiService
import com.example.composeapp.data.remote.categories.model.CategoryDto
import com.example.composeapp.data.remote.recipes.model.RecipeDto
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: RecipesApiService
) : RemoteDataSource {

    override suspend fun getCategories(): List<CategoryDto> {
        return apiService.getCategories()
    }

    override suspend fun getRecipesByCategoryId(categoryId: Int): List<RecipeDto> {
        return apiService.getRecipesByCategoryId(categoryId)
    }

    override suspend fun getRecipeById(recipeId: Int): RecipeDto? {
        return apiService.getRecipeById(recipeId)
    }
}