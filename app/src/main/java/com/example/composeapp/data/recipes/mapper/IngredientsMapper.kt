package com.example.composeapp.data.recipes.mapper

import com.example.composeapp.data.recipes.model.IngredientDto
import com.example.composeapp.domain.recipes.model.Ingredient

fun IngredientDto.toDomainModel() = Ingredient(
    quantity = quantity,
    unitOfMeasure = unitOfMeasure,
    description = description
)
