package com.pavlushinsa.recipescompapp.presentation.recipes.detail.mapper

import com.pavlushinsa.recipescompapp.domain.recipes.model.Ingredient
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.model.IngredientUiModel

fun Ingredient.toIngredientUiModel() = IngredientUiModel(
    quantity = quantity,
    unitOfMeasure = unitOfMeasure,
    description = description
)
