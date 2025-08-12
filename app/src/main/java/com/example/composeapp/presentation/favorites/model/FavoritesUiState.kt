package com.example.composeapp.presentation.favorites.model

import com.example.composeapp.presentation.recipes.list.model.RecipeCardUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FavoritesUiState(
    val recipes: ImmutableList<RecipeCardUiModel> = persistentListOf(),
    val isError: Boolean = false
)
