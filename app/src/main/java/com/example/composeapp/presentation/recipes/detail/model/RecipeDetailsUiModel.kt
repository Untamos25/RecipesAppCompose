package com.example.composeapp.presentation.recipes.detail.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class RecipeDetailsUiModel(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val ingredients: ImmutableList<IngredientUiModel>,
    val method: ImmutableList<String>,
    val isFavorite: Boolean
)
