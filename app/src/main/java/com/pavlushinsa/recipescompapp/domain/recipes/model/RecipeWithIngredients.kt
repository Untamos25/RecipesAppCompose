package com.pavlushinsa.recipescompapp.domain.recipes.model

data class RecipeWithIngredients(
    val recipe: Recipe,
    val ingredients: List<Ingredient>
)
