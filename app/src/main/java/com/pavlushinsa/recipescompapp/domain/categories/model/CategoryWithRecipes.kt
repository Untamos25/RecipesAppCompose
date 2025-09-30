package com.pavlushinsa.recipescompapp.domain.categories.model

import com.pavlushinsa.recipescompapp.domain.recipes.model.Recipe

data class CategoryWithRecipes(
    val category: Category,
    val recipes: List<Recipe>
)