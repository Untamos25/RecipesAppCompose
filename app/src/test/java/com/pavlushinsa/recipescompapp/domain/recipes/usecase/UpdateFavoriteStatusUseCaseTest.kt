package com.pavlushinsa.recipescompapp.domain.recipes.usecase

import com.pavlushinsa.recipescompapp.domain.repository.AppRepository
import io.mockk.MockKAnnotations
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateFavoriteStatusUseCaseTest {

    @RelaxedMockK
    private lateinit var appRepository: AppRepository

    private lateinit var updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        updateFavoriteStatusUseCase = UpdateFavoriteStatusUseCase(appRepository)
    }

    @Test
    fun `GIVEN favorite status update WHEN invoke THEN should call repository with correct parameters`() = runTest {
        // ARRANGE
        val testRecipeId = 1
        val isFavorite = true
        coJustRun { appRepository.updateFavoriteStatus(testRecipeId, isFavorite) }

        // ACT
        updateFavoriteStatusUseCase(testRecipeId, isFavorite)

        // ASSERT
        coVerify(exactly = 1) { appRepository.updateFavoriteStatus(testRecipeId, isFavorite) }
    }

    @Test
    fun `GIVEN isFavorite is false WHEN invoke THEN should call repository with correct parameters`() = runTest {
        // ARRANGE
        val testRecipeId = 2
        val isFavorite = false
        coJustRun { appRepository.updateFavoriteStatus(testRecipeId, isFavorite) }

        // ACT
        updateFavoriteStatusUseCase(testRecipeId, isFavorite)

        // ASSERT
        coVerify(exactly = 1) { appRepository.updateFavoriteStatus(testRecipeId, isFavorite) }
    }
}
