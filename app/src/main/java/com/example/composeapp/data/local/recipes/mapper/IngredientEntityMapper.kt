package com.example.composeapp.data.local.recipes.mapper

import com.example.composeapp.data.local.recipes.entity.IngredientEntity
import com.example.composeapp.domain.recipes.model.Ingredient

fun IngredientEntity.toDomain() = Ingredient(
    quantity = quantity,
    unitOfMeasure = unitOfMeasure,
    description = description
)
