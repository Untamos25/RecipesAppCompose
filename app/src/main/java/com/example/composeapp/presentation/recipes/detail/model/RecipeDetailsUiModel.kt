package com.example.composeapp.presentation.recipes.detail.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class RecipeDetailsUiModel(
    val id: Int,
    val title: String,
    val ingredients: ImmutableList<IngredientUiModel>,
    val method: ImmutableList<String>,
    val imageUrl: String
)
