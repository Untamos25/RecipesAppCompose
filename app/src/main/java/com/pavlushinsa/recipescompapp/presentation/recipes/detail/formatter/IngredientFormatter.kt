package com.pavlushinsa.recipescompapp.presentation.recipes.detail.formatter

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.pavlushinsa.recipescompapp.R

object IngredientFormatter {

    private const val PLURALS_FOR_FRACTIONAL_NUMBERS = 2

    private val stringResourceMap: Map<String, Int> = mapOf(
        "г" to R.string.unit_gram,
        "кг" to R.string.unit_weight,
        "мл" to R.string.unit_milliliter,
        "л" to R.string.unit_liter,
        "по вкусу" to R.string.unit_to_taste,
        "по вкусу г" to R.string.unit_to_taste,
        "несколько шт." to R.string.unit_several_pieces,
        "несколько листов" to R.string.unit_several_leaf
    )

    private val pluralResourceMap: Map<String, Int> = mapOf(
        "шт" to R.plurals.unit_piece,
        "шт." to R.plurals.unit_piece,
        "ломтик" to R.plurals.unit_slice,
        "лист" to R.plurals.unit_leaf,
        "зубч" to R.plurals.unit_clove,
        "зубчик" to R.plurals.unit_clove,
        "ст. л." to R.plurals.unit_tablespoon,
        "ст. ложка" to R.plurals.unit_tablespoon,
        "ст. ложки" to R.plurals.unit_tablespoon,
        "ч. л." to R.plurals.unit_teaspoon,
        "ч. ложка" to R.plurals.unit_teaspoon,
        "ч. ложки" to R.plurals.unit_teaspoon
    )

    @Composable
    fun formatUnitOfMeasure(quantity: String, unitOfMeasure: String): String {
        val unit = unitOfMeasure.lowercase()
        val quantityFloat = quantity.toFloatOrNull()

        val pluralResId = pluralResourceMap[unit]
        val stringResId = stringResourceMap[unit]

        return when {
            pluralResId != null && quantityFloat != null -> {
                val isInteger = quantityFloat % 1.0f == 0.0f

                val countForPlurals = if (isInteger) quantityFloat.toInt()
                else PLURALS_FOR_FRACTIONAL_NUMBERS

                pluralStringResource(id = pluralResId, count = countForPlurals)
            }

            stringResId != null -> stringResource(id = stringResId)

            else -> unitOfMeasure
        }
    }
}
