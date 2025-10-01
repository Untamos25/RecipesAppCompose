package com.pavlushinsa.recipescompapp.presentation.recipes.detail.model

import com.pavlushinsa.recipescompapp.domain.recipes.model.Ingredient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

const val INITIAL_PORTIONS = 1f

data class RecipeDetailsUiState(
    val recipe: RecipeDetailsUiModel? = null,
    val portionsCount: Float = INITIAL_PORTIONS,
    var ingredients: ImmutableList<Ingredient> = persistentListOf(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false
)
