package com.pavlushinsa.recipescompapp.presentation.recipes.list.mapper

import com.pavlushinsa.recipescompapp.domain.recipes.model.Recipe
import com.pavlushinsa.recipescompapp.presentation.recipes.list.model.RecipeCardUiModel

fun Recipe.toRecipeCardUiModel() = RecipeCardUiModel(
    id = id,
    title = title,
    imageUrl = imageUrl
)
