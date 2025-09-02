package com.example.composeapp.data.remote.recipes.mapper

import com.example.composeapp.data.local.recipes.entity.IngredientEntity
import com.example.composeapp.data.remote.recipes.model.IngredientDto

fun IngredientDto.toEntity(recipeId: Int, index: Int): IngredientEntity {

    val isQuantityNumeric = quantity.toFloatOrNull() != null

    val normalizedQuantity: String
    val normalizedUnitOfMeasure: String

    if (isQuantityNumeric) {
        normalizedQuantity = quantity
        normalizedUnitOfMeasure = unitOfMeasure
    } else {
        normalizedQuantity = ""
        normalizedUnitOfMeasure = "$quantity $unitOfMeasure".trim()
    }

    return IngredientEntity(
        recipeId = recipeId,
        ingredientIndex = index,
        quantity = normalizedQuantity,
        unitOfMeasure = normalizedUnitOfMeasure,
        description = description,
    )
}
