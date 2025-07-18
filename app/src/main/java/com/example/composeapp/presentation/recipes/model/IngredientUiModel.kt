package com.example.composeapp.presentation.recipes.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class IngredientUiModel(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String
) : Parcelable