package com.pavlushinsa.recipescompapp.presentation.recipes.detail.mapper

import com.pavlushinsa.recipescompapp.domain.recipes.model.RecipeWithIngredients
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.model.RecipeDetailsUiModel
import kotlinx.collections.immutable.toImmutableList

fun RecipeWithIngredients.toRecipeDetailsUiModel() = RecipeDetailsUiModel(
    id = recipe.id,
    title = recipe.title,
    imageUrl = recipe.imageUrl,
    ingredients = ingredients.map { it.toIngredientUiModel() }.toImmutableList(),
    method = recipe.method.toImmutableList(),
    isFavorite = recipe.isFavorite
)
