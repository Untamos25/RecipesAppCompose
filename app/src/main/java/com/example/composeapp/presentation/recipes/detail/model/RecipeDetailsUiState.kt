package com.example.composeapp.presentation.recipes.detail.model

import com.example.composeapp.domain.recipes.model.Ingredient
import com.example.composeapp.presentation.common.constants.SliderConstants.INITIAL_PORTIONS
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class RecipeDetailsUiState(
    val recipe: RecipeDetailsUiModel? = null,
    val portionsCount: Float = INITIAL_PORTIONS,
    var ingredients: ImmutableList<Ingredient> = persistentListOf(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false
)
