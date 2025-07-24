package com.example.composeapp.presentation.recipes.detail.formatter

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.example.composeapp.R
import com.example.composeapp.presentation.common.constants.AbbreviationConstants.UNIT_CLOVE_ABBREVIATION
import com.example.composeapp.presentation.common.constants.AbbreviationConstants.UNIT_PIECE_ABBREVIATION
import com.example.composeapp.presentation.common.constants.AbbreviationConstants.UNIT_TABLESPOON_ABBREVIATION
import com.example.composeapp.presentation.common.constants.AbbreviationConstants.UNIT_WEIGHT_ABBREVIATION
import com.example.composeapp.presentation.recipes.detail.model.IngredientUiModel

object IngredientFormatter {
    @Composable
    fun formatUnitOfMeasure(ingredient: IngredientUiModel): String {
        val quantityInt = ingredient.quantity.toFloatOrNull()?.toInt()
        return if (quantityInt != null) {
            when (ingredient.unitOfMeasure) {

                UNIT_WEIGHT_ABBREVIATION -> stringResource(R.string.unit_weight)

                UNIT_PIECE_ABBREVIATION -> pluralStringResource(
                    R.plurals.unit_piece,
                    quantityInt
                )

                UNIT_TABLESPOON_ABBREVIATION -> pluralStringResource(
                    R.plurals.unit_tablespoon,
                    quantityInt
                )

                UNIT_CLOVE_ABBREVIATION -> pluralStringResource(
                    R.plurals.unit_clove,
                    quantityInt
                )

                else -> ingredient.unitOfMeasure
            }
        } else {
            ingredient.unitOfMeasure
        }
    }
}
