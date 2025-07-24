package com.example.composeapp.presentation.recipes.detail.mapper

import com.example.composeapp.domain.recipes.model.Ingredient
import com.example.composeapp.presentation.common.constants.AbbreviationConstants.INGREDIENT_QUANTITY_FORMAT
import com.example.composeapp.presentation.recipes.detail.model.IngredientUiModel
import java.text.DecimalFormat

private val decimalFormat = DecimalFormat(INGREDIENT_QUANTITY_FORMAT)

fun Ingredient.toUiModel(): IngredientUiModel {

    val formattedQuantity = this.quantity.toFloatOrNull()?.let {
        decimalFormat.format(it)
    } ?: this.quantity

    return IngredientUiModel(
        quantity = formattedQuantity,
        unitOfMeasure = this.unitOfMeasure,
        description = this.description
    )
}

fun List<Ingredient>.toUiModelList(): List<IngredientUiModel> {
    return this.map { it.toUiModel() }
}
