package com.example.composeapp.domain.categories.model

import com.example.composeapp.domain.recipes.model.Recipe

data class CategoryWithRecipes(
    val category: Category,
    val recipes: List<Recipe>
)