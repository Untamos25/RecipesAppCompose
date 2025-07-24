package com.example.composeapp.presentation.recipes.list.mapper

import com.example.composeapp.domain.recipes.model.Recipe
import com.example.composeapp.presentation.common.constants.UIConstants.ASSETS_URI_PREFIX
import com.example.composeapp.presentation.recipes.list.model.RecipeCardUiModel

fun Recipe.toRecipeCardUiModel() = RecipeCardUiModel(
    id = id,
    title = title,
    imageUrl = ASSETS_URI_PREFIX + imageUrl
)
