package com.example.composeapp.presentation.recipes.mapper

import com.example.composeapp.domain.recipes.model.Ingredient
import com.example.composeapp.presentation.recipes.model.IngredientUiModel

fun Ingredient.toUiModel() = IngredientUiModel(
    quantity = quantity,
    unitOfMeasure = unitOfMeasure,
    description = description
)
