package com.example.composeapp.data.remote.recipes.mapper

import com.example.composeapp.data.local.recipes.entity.IngredientEntity
import com.example.composeapp.data.remote.recipes.model.IngredientDto

fun IngredientDto.toEntity(recipeId: Int, index: Int) = IngredientEntity(
    recipeId = recipeId,
    ingredientIndex = index,
    quantity = quantity,
    unitOfMeasure = unitOfMeasure,
    description = description,
)
