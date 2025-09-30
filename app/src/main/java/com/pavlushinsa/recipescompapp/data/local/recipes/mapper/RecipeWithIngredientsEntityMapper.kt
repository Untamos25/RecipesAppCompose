package com.pavlushinsa.recipescompapp.data.local.recipes.mapper

import com.pavlushinsa.recipescompapp.data.local.recipes.RecipeWithIngredientsEntity
import com.pavlushinsa.recipescompapp.domain.recipes.model.RecipeWithIngredients

fun RecipeWithIngredientsEntity.toDomain() = RecipeWithIngredients(
    recipe = recipeEntity.toDomain(),
    ingredients = ingredientEntities.map { it.toDomain() }
)
