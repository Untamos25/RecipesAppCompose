package com.pavlushinsa.recipescompapp.data.local.recipes.mapper

import com.pavlushinsa.recipescompapp.data.local.recipes.entity.IngredientEntity
import com.pavlushinsa.recipescompapp.domain.recipes.model.Ingredient

fun IngredientEntity.toDomain() = Ingredient(
    quantity = quantity,
    unitOfMeasure = unitOfMeasure,
    description = description
)
