package com.pavlushinsa.recipescompapp.presentation.recipes.detail.model

import androidx.compose.runtime.Immutable

@Immutable
data class IngredientUiModel(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String
)
