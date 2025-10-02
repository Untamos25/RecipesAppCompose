package com.pavlushinsa.recipescompapp.presentation.recipes.detail.model

const val INITIAL_PORTIONS = 1f

data class RecipeDetailsUiState(
    val recipe: RecipeDetailsUiModel? = null,
    val portionsCount: Float = INITIAL_PORTIONS,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false
)
