package com.pavlushinsa.recipescompapp.data.remote.api

import com.pavlushinsa.recipescompapp.data.remote.categories.model.CategoryDto
import com.pavlushinsa.recipescompapp.data.remote.recipes.model.RecipeDto
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
