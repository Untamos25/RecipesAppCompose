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

class SyncRecipesForCategoryUseCaseTest {

    @RelaxedMockK
    private lateinit var appRepository: AppRepository

    private lateinit var syncRecipesForCategoryUseCase: SyncRecipesForCategoryUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        syncRecipesForCategoryUseCase = SyncRecipesForCategoryUseCase(appRepository)
    }

    @Test
    fun `GIVEN sync is successful WHEN invoke with categoryId THEN should return success`() = runTest {
        // ARRANGE
        val testCategoryId = 123
        val expectedResult = DataResult.Success(Unit)
        coEvery { appRepository.syncRecipesForCategory(testCategoryId) } returns expectedResult

        // ACT
        val actualResult = syncRecipesForCategoryUseCase(testCategoryId)

        // ASSERT
        assertEquals(expectedResult, actualResult)
        coVerify(exactly = 1) { appRepository.syncRecipesForCategory(testCategoryId) }
    }

    @Test
    fun `GIVEN sync fails WHEN invoke with categoryId THEN should return failure`() = runTest {
        // ARRANGE
        val testCategoryId = 123
        val expectedError = Error.NoInternetConnection
        val expectedResult = DataResult.Failure(expectedError)
        coEvery { appRepository.syncRecipesForCategory(testCategoryId) } returns expectedResult

        // ACT
        val actualResult = syncRecipesForCategoryUseCase(testCategoryId)

        // ASSERT
        assertEquals(expectedResult, actualResult)
        coVerify(exactly = 1) { appRepository.syncRecipesForCategory(testCategoryId) }
    }
}
