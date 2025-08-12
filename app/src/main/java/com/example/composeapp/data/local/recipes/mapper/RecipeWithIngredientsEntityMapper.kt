package com.example.composeapp.data.local.recipes.mapper

import com.example.composeapp.data.local.recipes.RecipeWithIngredientsEntity
import com.example.composeapp.domain.recipes.model.RecipeWithIngredients

fun RecipeWithIngredientsEntity.toDomain() = RecipeWithIngredients(
    recipe = recipeEntity.toDomain(),
    ingredients = ingredientEntities.map { it.toDomain() }
)
