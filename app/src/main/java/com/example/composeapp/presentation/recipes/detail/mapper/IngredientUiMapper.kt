package com.example.composeapp.presentation.recipes.detail.mapper

import com.example.composeapp.domain.recipes.model.Ingredient
import com.example.composeapp.presentation.recipes.detail.model.IngredientUiModel

fun Ingredient.toIngredientUiModel() = IngredientUiModel(
    quantity = quantity,
    unitOfMeasure = unitOfMeasure,
    description = description
)
