package com.example.composeapp.presentation.recipes.detail.mapper

import com.example.composeapp.domain.recipes.model.Recipe
import com.example.composeapp.presentation.common.constants.UIConstants.ASSETS_URI_PREFIX
import com.example.composeapp.presentation.recipes.detail.model.RecipeDetailsUiModel
import kotlinx.collections.immutable.toImmutableList

fun Recipe.toRecipeDetailUiModel() = RecipeDetailsUiModel(
    id = id,
    title = title,
    ingredients = ingredients.toUiModelList().toImmutableList(),
    method = method.toImmutableList(),
    imageUrl = ASSETS_URI_PREFIX + imageUrl
)
