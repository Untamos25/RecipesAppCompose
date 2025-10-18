package com.pavlushinsa.recipescompapp.domain.recipes.usecase

import com.pavlushinsa.recipescompapp.domain.common.DataResult
import com.pavlushinsa.recipescompapp.domain.common.Error
import com.pavlushinsa.recipescompapp.domain.repository.AppRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SyncRecipeDetailsUseCaseTest {

    @RelaxedMockK
    private lateinit var appRepository: AppRepository

    private lateinit var syncRecipeDetailsUseCase: SyncRecipeDetailsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        syncRecipeDetailsUseCase = SyncRecipeDetailsUseCase(appRepository)
    }

    @Test
    fun `GIVEN sync is successful WHEN invoke with recipeId THEN should return success`() = runTest {
        // ARRANGE
        val testRecipeId = 456
        val expectedResult = DataResult.Success(Unit)
        coEvery { appRepository.syncRecipeDetails(testRecipeId) } returns expectedResult

        // ACT
        val actualResult = syncRecipeDetailsUseCase(testRecipeId)

        // ASSERT
        assertEquals(expectedResult, actualResult)
        coVerify(exactly = 1) { appRepository.syncRecipeDetails(testRecipeId) }
    }

    @Test
    fun `GIVEN sync fails WHEN invoke with recipeId THEN should return failure`() = runTest {
        // ARRANGE
        val testRecipeId = 456
        val expectedError = Error.NoInternetConnection
        val expectedResult = DataResult.Failure(expectedError)
        coEvery { appRepository.syncRecipeDetails(testRecipeId) } returns expectedResult

        // ACT
        val actualResult = syncRecipeDetailsUseCase(testRecipeId)

        // ASSERT
        assertEquals(expectedResult, actualResult)
        coVerify(exactly = 1) { appRepository.syncRecipeDetails(testRecipeId) }
    }
}
