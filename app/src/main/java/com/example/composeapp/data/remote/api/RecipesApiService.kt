package com.example.composeapp.data.remote.api

import com.example.composeapp.data.remote.categories.model.CategoryDto
import com.example.composeapp.data.remote.recipes.model.RecipeDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipesApiService {

    @GET("category")
    suspend fun getCategories(): List<CategoryDto>

    @GET("category/{id}/recipes")
    suspend fun getRecipesByCategoryId(@Path("id") categoryId: Int): List<RecipeDto>

    @GET("recipe/{id}")
    suspend fun getRecipeById(@Path("id") recipeId: Int): RecipeDto?
}
