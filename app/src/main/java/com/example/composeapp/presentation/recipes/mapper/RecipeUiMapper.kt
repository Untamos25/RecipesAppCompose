package com.example.composeapp.presentation.recipes.mapper

import com.example.composeapp.data.common.ASSETS_URI_PREFIX
import com.example.composeapp.domain.recipes.model.Recipe
import com.example.composeapp.presentation.recipes.model.RecipeUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

fun Recipe.toRecipeDetailUiModel() = RecipeUiModel(
    id = id,
    title = title,
    ingredients = ingredients.map { it.toUiModel() }.toImmutableList(),
    method = method.toImmutableList(),
    imageUrl = imageUrl
)

fun Recipe.toRecipeCardUiModel() = RecipeUiModel(
    id = id,
    title = title,
    ingredients = persistentListOf(),
    method = persistentListOf(),
    imageUrl = imageUrl
)