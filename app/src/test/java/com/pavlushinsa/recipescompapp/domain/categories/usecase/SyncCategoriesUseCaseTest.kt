package com.pavlushinsa.recipescompapp.domain.categories.usecase

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

class SyncCategoriesUseCaseTest {

    @RelaxedMockK
    private lateinit var appRepository: AppRepository

    private lateinit var syncCategoriesUseCase: SyncCategoriesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        syncCategoriesUseCase = SyncCategoriesUseCase(appRepository)
    }

    @Test
    fun `GIVEN sync is successful WHEN invoke THEN should return success`() = runTest {
        // ARRANGE
        val expectedResult = DataResult.Success(Unit)
        coEvery { appRepository.syncCategories() } returns expectedResult

        // ACT
        val actualResult = syncCategoriesUseCase()

        // ASSERT
        assertEquals(expectedResult, actualResult)
        coVerify(exactly = 1) { appRepository.syncCategories() }
    }

    @Test
    fun `GIVEN sync fails WHEN invoke THEN should return failure`() = runTest {
        // ARRANGE
        val expectedError = Error.NoInternetConnection
        val expectedResult = DataResult.Failure(expectedError)
        coEvery { appRepository.syncCategories() } returns expectedResult

        // ACT
        val actualResult = syncCategoriesUseCase()

        // ASSERT
        assertEquals(expectedResult, actualResult)
        coVerify(exactly = 1) { appRepository.syncCategories() }
    }
}
