package com.example.composeapp.data.remote.recipes.model

import kotlinx.serialization.Serializable

@Serializable
data class IngredientDto(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String
)
