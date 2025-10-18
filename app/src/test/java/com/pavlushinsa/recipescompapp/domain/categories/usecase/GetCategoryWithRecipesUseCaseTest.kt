package com.pavlushinsa.recipescompapp.domain.categories.usecase

import com.pavlushinsa.recipescompapp.domain.categories.model.Category
import com.pavlushinsa.recipescompapp.domain.categories.model.CategoryWithRecipes
import com.pavlushinsa.recipescompapp.domain.recipes.model.Recipe
import com.pavlushinsa.recipescompapp.domain.repository.AppRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetCategoryWithRecipesUseCaseTest {

    @RelaxedMockK
    private lateinit var appRepository: AppRepository

    private lateinit var getCategoryWithRecipesUseCase: GetCategoryWithRecipesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCategoryWithRecipesUseCase = GetCategoryWithRecipesUseCase(appRepository)
    }

    @Suppress("MaxLineLength")
    @Test
    fun `GIVEN use case is invoked with category ID WHEN invoke THEN should call getCategoryWithRecipes with ID and return result`() =
        runTest {
            // ARRANGE
            val testCategoryId = 123
            val expectedResult = CategoryWithRecipes(
                category = Category(testCategoryId, "Test Category", "", "", 0L),
                recipes = listOf(
                    Recipe(
                        1,
                        testCategoryId,
                        "Test Recipe",
                        emptyList(),
                        "",
                        false,
                        0L
                    )
                )
            )
            val expectedFlow = flowOf(expectedResult)

            every { appRepository.getCategoryWithRecipes(testCategoryId) } returns expectedFlow

            // ACT
            val actualFlow = getCategoryWithRecipesUseCase(testCategoryId)

            // ASSERT
            Assert.assertEquals(expectedFlow, actualFlow)
            verify(exactly = 1) { appRepository.getCategoryWithRecipes(testCategoryId) }
        }
}
