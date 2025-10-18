package com.pavlushinsa.recipescompapp.domain.recipes.usecase

import com.pavlushinsa.recipescompapp.domain.recipes.model.Ingredient
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateIngredientsUseCaseTest {

    private val calculateIngredientsUseCase = CalculateIngredientsUseCase()

    @Test
    fun `GIVEN new portions are double WHEN invoke THEN should return ingredients with double quantity`() {

        // ARRANGE
        val originalIngredients = listOf(
            Ingredient(quantity = "100", unitOfMeasure = "г", description = "Мука"),
            Ingredient(quantity = "1.5", unitOfMeasure = "шт", description = "Яйцо"),
            Ingredient(quantity = "по вкусу", unitOfMeasure = "", description = "Соль")
        )
        val initialPortions = 1.0f
        val newPortions = 2.0f

        val expectedIngredients = listOf(
            Ingredient(quantity = "200.0", unitOfMeasure = "г", description = "Мука"),
            Ingredient(quantity = "3.0", unitOfMeasure = "шт", description = "Яйцо"),
            Ingredient(quantity = "по вкусу", unitOfMeasure = "", description = "Соль")
        )

        // ACT
        val actualResult = calculateIngredientsUseCase(
            originalIngredients = originalIngredients,
            initialPortions = initialPortions,
            newPortions = newPortions
        )

        // ASSERT
        assertEquals(expectedIngredients.size, actualResult.size)
        assertEquals(expectedIngredients[0].quantity, actualResult[0].quantity)
        assertEquals(expectedIngredients[1].quantity, actualResult[1].quantity)
        assertEquals(expectedIngredients[2].quantity, actualResult[2].quantity)
    }

    @Test
    fun `GIVEN portions are same WHEN invoke THEN should return original ingredients`() {
        // ARRANGE
        val originalIngredients = listOf(
            Ingredient(quantity = "100", unitOfMeasure = "г", description = "Мука")
        )
        val initialPortions = 2.0f
        val newPortions = 2.0f

        // ACT
        val actualResult = calculateIngredientsUseCase(
            originalIngredients = originalIngredients,
            initialPortions = initialPortions,
            newPortions = newPortions
        )

        // ASSERT
        assertEquals(originalIngredients, actualResult)
    }
}
