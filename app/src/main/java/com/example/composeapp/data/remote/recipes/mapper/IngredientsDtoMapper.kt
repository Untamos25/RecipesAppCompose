package com.example.composeapp.data.remote.recipes.mapper

import com.example.composeapp.data.local.recipes.entity.IngredientEntity
import com.example.composeapp.data.remote.recipes.model.IngredientDto

fun IngredientDto.toEntity(recipeId: Int) = IngredientEntity(
    quantity = quantity,
    recipeId = recipeId,
    unitOfMeasure = unitOfMeasure,
    description = description,
)
