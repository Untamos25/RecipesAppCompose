package com.pavlushinsa.recipescompapp.domain.categories.usecase

import com.pavlushinsa.recipescompapp.domain.categories.model.Category
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

class GetCategoriesUseCaseTest {

    @RelaxedMockK
    private lateinit var appRepository: AppRepository

    private lateinit var getCategoriesUseCase: GetCategoriesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCategoriesUseCase = GetCategoriesUseCase(appRepository)
    }

    @Test
    fun `GIVEN use case is invoked WHEN invoke THEN should call getCategories on repository and return its result`() =
        runTest {
            // ARRANGE
            val expectedCategories = listOf(
                Category(1, "Category 1", "Desc 1", "url1", System.currentTimeMillis())
            )
            val expectedFlow = flowOf(expectedCategories)
            every { appRepository.getCategories() } returns expectedFlow

            // ACT
            val actualFlow = getCategoriesUseCase()

            // ASSERT
            assertEquals(expectedFlow, actualFlow)
            verify(exactly = 1) { appRepository.getCategories() }
        }
}
