package com.pavlushinsa.recipescompapp.domain.recipes.usecase

import com.pavlushinsa.recipescompapp.domain.recipes.model.Recipe
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

class GetFavoriteRecipesUseCaseTest {

    @RelaxedMockK
    private lateinit var appRepository: AppRepository

    private lateinit var getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getFavoriteRecipesUseCase = GetFavoriteRecipesUseCase(appRepository)
    }

    @Suppress("MaxLineLength")
    @Test
    fun `GIVEN use case is invoked WHEN invoke THEN should call getFavoriteRecipes on repository and return its result`() = runTest {
        // ARRANGE
        val expectedFavoriteRecipes = listOf(
            Recipe(1, 1, "Favorite Recipe", emptyList(), "", true, 0L)
        )
        val expectedFlow = flowOf(expectedFavoriteRecipes)

        every { appRepository.getFavoriteRecipes() } returns expectedFlow

        // ACT
        val actualFlow = getFavoriteRecipesUseCase()

        // ASSERT
        assertEquals(expectedFlow, actualFlow)
        verify(exactly = 1) { appRepository.getFavoriteRecipes() }
    }
}
