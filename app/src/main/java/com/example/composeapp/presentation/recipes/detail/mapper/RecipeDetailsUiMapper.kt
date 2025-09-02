package com.example.composeapp.presentation.recipes.detail.mapper

import com.example.composeapp.domain.recipes.model.RecipeWithIngredients
import com.example.composeapp.presentation.recipes.detail.model.RecipeDetailsUiModel
import kotlinx.collections.immutable.toImmutableList

fun RecipeWithIngredients.toRecipeDetailsUiModel() = RecipeDetailsUiModel(
    id = recipe.id,
    title = recipe.title,
    imageUrl = recipe.imageUrl,
    ingredients = ingredients.map { it.toIngredientUiModel() }.toImmutableList(),
    method = recipe.method.toImmutableList(),
    isFavorite = recipe.isFavorite
)
