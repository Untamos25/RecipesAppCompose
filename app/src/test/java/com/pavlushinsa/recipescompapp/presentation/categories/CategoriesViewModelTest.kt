package com.pavlushinsa.recipescompapp.presentation.categories

import android.database.sqlite.SQLiteException
import app.cash.turbine.test
import com.pavlushinsa.recipescompapp.util.BaseViewModelTest
import com.pavlushinsa.recipescompapp.domain.categories.model.Category
import com.pavlushinsa.recipescompapp.domain.categories.usecase.GetCategoriesUseCase
import com.pavlushinsa.recipescompapp.domain.categories.usecase.SyncCategoriesUseCase
import com.pavlushinsa.recipescompapp.domain.common.DataResult
import com.pavlushinsa.recipescompapp.domain.common.Error
import com.pavlushinsa.recipescompapp.presentation.common.AppWideEventDelegate
import com.pavlushinsa.recipescompapp.presentation.common.mapper.toUiErrorType
import com.pavlushinsa.recipescompapp.presentation.common.model.UiErrorType
import com.pavlushinsa.recipescompapp.presentation.common.model.UiEvent
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CategoriesViewModelTest : BaseViewModelTest() {

    @RelaxedMockK
    private lateinit var getCategoriesUseCase: GetCategoriesUseCase

    @RelaxedMockK
    private lateinit var syncCategoriesUseCase: SyncCategoriesUseCase

    @RelaxedMockK
    private lateinit var eventDelegate: AppWideEventDelegate

    private lateinit var viewModel: CategoriesViewModel

    @Before
    override fun setUp() {
        super.setUp()
        MockKAnnotations.init(this)
    }

    // region Initialization and Data Loading

    @Test
    fun `GIVEN fresh cache WHEN initialized THEN should load local data and not trigger sync`() =
        runTest {
            // ARRANGE
            val freshCategories = listOf(
                Category(1, "Category 1", "Desc 1", "url1", System.currentTimeMillis()),
                Category(2, "Category 2", "Desc 2", "url2", System.currentTimeMillis())
            )
            coEvery { getCategoriesUseCase() } returns flowOf(freshCategories)

            // ACT
            viewModel =
                CategoriesViewModel(getCategoriesUseCase, syncCategoriesUseCase, eventDelegate)

            // ASSERT
            viewModel.categoriesUiState.test {
                awaitItem() // initial state
                awaitItem() // data loaded

                val finalState = awaitItem()
                assertEquals(false, finalState.isLoading)
                assertEquals("Category 1", finalState.categories[0].title)
                assertEquals(2, finalState.categories.size)

                expectNoEvents()
            }

            advanceUntilIdle()

            // VERIFY
            coVerify(atLeast = 1) { getCategoriesUseCase() }
            coVerify(exactly = 0) { syncCategoriesUseCase() }
            coVerify(exactly = 0) { eventDelegate.sendAppWideEvent(any()) }
        }

    @Test
    fun `GIVEN stale cache WHEN initialized THEN should load local data and trigger sync`() =
        runTest {
            // ARRANGE
            val staleCategories = listOf(
                Category(1, "Stale Category 1", "Desc 1", "url1", 0L),
                Category(2, "Stale Category 2", "Desc 2", "url2", 0L)
            )
            coEvery { getCategoriesUseCase() } returns flowOf(staleCategories)
            coEvery { syncCategoriesUseCase() } returns DataResult.Success(Unit)

            // ACT
            viewModel =
                CategoriesViewModel(getCategoriesUseCase, syncCategoriesUseCase, eventDelegate)

            // ASSERT
            viewModel.categoriesUiState.test {
                awaitItem() // initial state

                val dataLoadedState = awaitItem()
                assertEquals(true, dataLoadedState.isLoading)
                assertEquals(2, dataLoadedState.categories.size)
                assertEquals("Stale Category 1", dataLoadedState.categories[0].title)

                val finalState = awaitItem()
                assertEquals(false, finalState.isLoading)
                assertEquals(2, finalState.categories.size)

                cancelAndIgnoreRemainingEvents()
            }

            advanceUntilIdle()

            // VERIFY
            coVerify(atLeast = 1) { getCategoriesUseCase() }
            coVerify(exactly = 1) { syncCategoriesUseCase() }
            coVerify(exactly = 0) { eventDelegate.sendAppWideEvent(any()) }
        }

    @Test
    fun `GIVEN empty cache WHEN initialized THEN should trigger sync`() = runTest {
        // ARRANGE
        coEvery { getCategoriesUseCase() } returns flowOf(emptyList())
        coEvery { syncCategoriesUseCase() } returns DataResult.Success(Unit)

        // ACT
        viewModel = CategoriesViewModel(getCategoriesUseCase, syncCategoriesUseCase, eventDelegate)

        // ASSERT
        viewModel.categoriesUiState.test {
            val initialState = awaitItem()
            assertEquals(true, initialState.isLoading)
            assertEquals(true, initialState.categories.isEmpty())

            val finalState = awaitItem()
            assertEquals(false, finalState.isLoading)
            assertEquals(true, finalState.categories.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }

        advanceUntilIdle()

        // VERIFY
        coVerify(atLeast = 1) { getCategoriesUseCase() }
        coVerify(exactly = 1) { syncCategoriesUseCase() }
    }

    @Test
    fun `GIVEN sync fails WHEN initialized THEN should show cached data and send error event`() =
        runTest {
            // ARRANGE
            val staleCategories = listOf(
                Category(1, "Stale Category 1", "Desc 1", "url1", 0L)
            )
            val testError = Error.NoInternetConnection
            val eventSlot = slot<UiEvent>()

            coEvery { getCategoriesUseCase() } returns flowOf(staleCategories)
            coEvery { syncCategoriesUseCase() } returns DataResult.Failure(testError)

            // ACT
            viewModel =
                CategoriesViewModel(getCategoriesUseCase, syncCategoriesUseCase, eventDelegate)

            // ASSERT
            viewModel.categoriesUiState.test {
                awaitItem() // initial state

                val dataLoadedState = awaitItem()
                assertEquals(1, dataLoadedState.categories.size)
                assertEquals("Stale Category 1", dataLoadedState.categories.first().title)

                val finalState = awaitItem()
                assertEquals(false, finalState.isLoading)

                cancelAndIgnoreRemainingEvents()
            }

            advanceUntilIdle()

            // VERIFY
            coVerify(exactly = 1) { syncCategoriesUseCase() }
            coVerify(exactly = 1) { eventDelegate.sendAppWideEvent(capture(eventSlot)) }

            val capturedEvent = eventSlot.captured
            assert(capturedEvent is UiEvent.ShowSnackBarEvent)
            assertEquals(
                testError.toUiErrorType(),
                (capturedEvent as UiEvent.ShowSnackBarEvent).errorType
            )
        }

    @Test
    fun `GIVEN database read error WHEN initialized THEN should stop loading and send error event`() =
        runTest {
            // ARRANGE
            val testException = SQLiteException("Test DB Error")
            val eventSlot = slot<UiEvent>()

            coEvery { getCategoriesUseCase() } returns flow { throw testException }

            // ACT
            viewModel =
                CategoriesViewModel(getCategoriesUseCase, syncCategoriesUseCase, eventDelegate)

            advanceUntilIdle()

            // ASSERT
            viewModel.categoriesUiState.test {
                val finalState = awaitItem()
                assertEquals(false, finalState.isLoading)
                assertEquals(true, finalState.categories.isEmpty())
            }

            // VERIFY
            coVerify(exactly = 1) { eventDelegate.sendAppWideEvent(capture(eventSlot)) }
            val capturedEvent = eventSlot.captured
            assert(capturedEvent is UiEvent.ShowSnackBarEvent)
            assertEquals(
                UiErrorType.Database,
                (capturedEvent as UiEvent.ShowSnackBarEvent).errorType
            )

            coVerify(exactly = 0) { syncCategoriesUseCase() }
        }

    // endregion

    // region Pull-to-Refresh

    @Test
    fun `GIVEN user requests refresh WHEN sync is successful THEN should show refreshing state`() =
        runTest {
            // ARRANGE
            coEvery { getCategoriesUseCase() } returns flowOf(emptyList())
            coEvery { syncCategoriesUseCase() } returns DataResult.Success(Unit)

            viewModel =
                CategoriesViewModel(getCategoriesUseCase, syncCategoriesUseCase, eventDelegate)

            advanceUntilIdle()

            viewModel.categoriesUiState.test {
                awaitItem() // initial state

                // ACT
                viewModel.onRefresh()

                // ASSERT
                val refreshingState = awaitItem()
                assertEquals(true, refreshingState.isRefreshing)

                val finalState = awaitItem()
                assertEquals(false, finalState.isRefreshing)

                cancelAndIgnoreRemainingEvents()
            }

            // VERIFY
            coVerify(exactly = 2) { syncCategoriesUseCase() }
        }

    @Test
    fun `GIVEN user requests refresh WHEN sync fails THEN should show refreshing state and send error event`() =
        runTest {
            // ARRANGE
            val testError = Error.ServerError
            val eventList = mutableListOf<UiEvent>()

            coEvery { getCategoriesUseCase() } returns flowOf(emptyList())
            coEvery { syncCategoriesUseCase() } returns DataResult.Failure(testError)

            viewModel =
                CategoriesViewModel(getCategoriesUseCase, syncCategoriesUseCase, eventDelegate)

            advanceUntilIdle()

            viewModel.categoriesUiState.test {
                awaitItem() // initial state

                // ACT
                viewModel.onRefresh()

                // ASSERT
                assertEquals(true, awaitItem().isRefreshing)
                assertEquals(false, awaitItem().isRefreshing)

                cancelAndIgnoreRemainingEvents()
            }

            // VERIFY
            coVerify(exactly = 2) { syncCategoriesUseCase() }
            coVerify(exactly = 2) { eventDelegate.sendAppWideEvent(capture(eventList)) }

            assertEquals(2, eventList.size)
            eventList.forEach { capturedEvent ->
                assert(capturedEvent is UiEvent.ShowSnackBarEvent)
                assertEquals(
                    testError.toUiErrorType(),
                    (capturedEvent as UiEvent.ShowSnackBarEvent).errorType
                )
            }
        }

    // endregion
}
