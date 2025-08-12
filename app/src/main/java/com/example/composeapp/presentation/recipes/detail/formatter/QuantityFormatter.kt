package com.example.composeapp.presentation.recipes.detail.formatter

import java.text.DecimalFormat

object QuantityFormatter {
    private val formatter = DecimalFormat("#.##")

    fun format(quantity: String): String {
        return quantity.toFloatOrNull()?.let {
            formatter.format(it)
        } ?: quantity
    }
}
