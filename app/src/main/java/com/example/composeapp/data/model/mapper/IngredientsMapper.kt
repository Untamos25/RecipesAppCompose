package com.example.composeapp.data.model.mapper

import com.example.composeapp.data.model.IngredientDto
import com.example.composeapp.ui.model.IngredientUiModel

fun IngredientDto.toUiModel() = IngredientUiModel(
    quantity = quantity,
    unitOfMeasure = unitOfMeasure,
    description = description
)