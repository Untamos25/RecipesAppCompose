package com.example.composeapp.data.model.mapper

import com.example.composeapp.data.ASSETS_URI_PREFIX
import com.example.composeapp.data.model.RecipeDto
import com.example.composeapp.ui.model.RecipeUiModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

fun RecipeDto.toUiModel() = RecipeUiModel(
    id = id,
    title = title,
    ingredients = ingredients.map { it.toUiModel() }.toImmutableList(),
    method = method.toImmutableList(),
    imageUrl = ASSETS_URI_PREFIX + imageUrl
)

fun RecipeDto.toRecipeCardUiModel() = RecipeUiModel(
    id = id,
    title = title,
    ingredients = persistentListOf(),
    method = persistentListOf(),
    imageUrl = ASSETS_URI_PREFIX + imageUrl
)