package com.example.composeapp.presentation.recipes.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class RecipeUiModel(
    val id: Int,
    val title: String,
    val ingredients: ImmutableList<IngredientUiModel>,
    val method: ImmutableList<String>,
    val imageUrl: String
) : Parcelable