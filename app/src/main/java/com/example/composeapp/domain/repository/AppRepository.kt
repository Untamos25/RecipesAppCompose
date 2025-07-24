package com.example.composeapp.domain.repository

import com.example.composeapp.domain.categories.model.Category
import com.example.composeapp.domain.recipes.model.Recipe

interface AppRepository {
    suspend fun getCategories(): List<Category>
    suspend fun getCategoryById(categoryId: Int): Category?
    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>
    suspend fun getRecipeById(recipeId: Int): Recipe?
}
