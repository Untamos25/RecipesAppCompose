package com.pavlushinsa.recipescompapp.presentation.recipes.list.model

import androidx.compose.runtime.Immutable

@Immutable
data class RecipeCardUiModel(
    val id: Int,
    val title: String,
    val imageUrl: String
)
