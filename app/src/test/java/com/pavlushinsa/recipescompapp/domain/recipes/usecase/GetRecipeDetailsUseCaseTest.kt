package com.pavlushinsa.recipescompapp.domain.recipes.usecase

import com.pavlushinsa.recipescompapp.domain.recipes.model.Ingredient
import com.pavlushinsa.recipescompapp.domain.recipes.model.Recipe
import com.pavlushinsa.recipescompapp.domain.recipes.model.RecipeWithIngredients
import com.pavlushinsa.recipescompapp.domain.repository.AppRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetRecipeDetailsUseCaseTest {

    @RelaxedMockK
    private lateinit var appRepository: AppRepository

    private lateinit var getRecipeDetailsUseCase: GetRecipeDetailsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getRecipeDetailsUseCase = GetRecipeDetailsUseCase(appRepository)
    }

    @Suppress("MaxLineLength")
    @Test
    fun `GIVEN use case is invoked with recipe ID WHEN invoke THEN should call getRecipeWithIngredients with ID and return result`() =
        runTest {
            // ARRANGE
            val testRecipeId = 456
            val expectedResult = RecipeWithIngredients(
                recipe = Recipe(testRecipeId, 1, "Test Recipe", emptyList(), "", false, 0L),
                ingredients = listOf(Ingredient("1", "шт", "Test Ingredient"))
            )
            val expectedFlow = flowOf(expectedResult)

            every { appRepository.getRecipeWithIngredients(testRecipeId) } returns expectedFlow

            // ACT
            val actualFlow = getRecipeDetailsUseCase(testRecipeId)

            // ASSERT
            assertEquals(expectedFlow, actualFlow)
            verify(exactly = 1) { appRepository.getRecipeWithIngredients(testRecipeId) }
        }
}
