package com.pavlushinsa.recipescompapp.presentation.recipes.list.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RecipesListUiState(
    val categoryTitle: String = "",
    val categoryImageUrl: String? = null,
    val recipes: ImmutableList<RecipeCardUiModel> = persistentListOf(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
)
