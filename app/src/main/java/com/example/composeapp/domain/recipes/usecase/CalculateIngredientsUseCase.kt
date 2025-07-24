package com.example.composeapp.domain.recipes.usecase

import com.example.composeapp.domain.recipes.model.Ingredient
import javax.inject.Inject

class CalculateIngredientsUseCase @Inject constructor() {
    operator fun invoke(
        originalIngredients: List<Ingredient>,
        initialPortions: Float,
        newPortions: Float
    ): List<Ingredient> {

        if (initialPortions <= 0 || initialPortions == newPortions) {
            return originalIngredients
        }

        return originalIngredients.map { ingredient ->
            ingredient.quantity.toFloatOrNull()?.let { originalQuantity ->
                val newQuantity = (originalQuantity / initialPortions) * newPortions
                ingredient.copy(
                    quantity = newQuantity.toString()
                )
            } ?: ingredient
        }
    }
}
