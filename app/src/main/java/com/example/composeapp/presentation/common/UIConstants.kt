package com.example.composeapp.presentation.common

object UIConstants {
    // Параметры для слайдера
    const val MIN_PORTIONS = 1f
    const val MAX_PORTIONS = 6f
    const val PORTIONS_SLIDER_STEPS = 4
    const val INITIAL_PORTIONS = 1f

    // Параметры анимации для слайдера
    const val SLIDER_THUMB_SCALE_DEFAULT = 1.0f
    const val SLIDER_THUMB_SCALE_DRAGGED = 1.5f

    // Форматы и строки
    const val INGREDIENT_QUANTITY_FORMAT = "#.##"
    const val UNIT_PIECE_ABBREVIATION = "шт"
    const val UNIT_TABLESPOON_ABBREVIATION = "ст. л."
    const val UNIT_CLOVE_ABBREVIATION = "зубч"

    // Константы навигации
    const val CATEGORY_ID = "categoryId"
    const val RECIPE_ID = "recipeId"
}
