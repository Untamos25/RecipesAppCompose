package com.example.composeapp.presentation.recipes.detail.model

import com.example.composeapp.presentation.common.constants.SliderConstants.INITIAL_PORTIONS

data class RecipeDetailsUiState(
    val recipe: RecipeDetailsUiModel? = null,
    val portionsCount: Float = INITIAL_PORTIONS,
    val isError: Boolean = false
)
