package com.pavlushinsa.recipescompapp.presentation.favorites.model

import com.pavlushinsa.recipescompapp.presentation.recipes.list.model.RecipeCardUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FavoritesUiState(
    val recipes: ImmutableList<RecipeCardUiModel> = persistentListOf()
)
