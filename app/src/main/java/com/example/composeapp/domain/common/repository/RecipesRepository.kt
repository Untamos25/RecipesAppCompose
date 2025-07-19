package com.example.composeapp.domain.common.repository

import com.example.composeapp.domain.categories.model.Category
import com.example.composeapp.domain.recipes.model.Recipe

interface RecipesRepository {
    fun getCategories(): List<Category>
    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>
    fun getRecipeById(recipeId: Int): Recipe?
}