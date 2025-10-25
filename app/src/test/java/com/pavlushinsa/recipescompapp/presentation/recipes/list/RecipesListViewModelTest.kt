package com.pavlushinsa.recipescompapp.presentation.recipes.list

import android.database.sqlite.SQLiteException
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.pavlushinsa.recipescompapp.util.BaseViewModelTest
import com.pavlushinsa.recipescompapp.domain.categories.model.Category
import com.pavlushinsa.recipescompapp.domain.categories.model.CategoryWithRecipes
import com.pavlushinsa.recipescompapp.domain.categories.usecase.GetCategoryWithRecipesUseCase
import com.pavlushinsa.recipescompapp.domain.common.DataResult
import com.pavlushinsa.recipescompapp.domain.common.Error
import com.pavlushinsa.recipescompapp.domain.recipes.model.Recipe
import com.pavlushinsa.recipescompapp.domain.recipes.usecase.SyncRecipesForCategoryUseCase
import com.pavlushinsa.recipescompapp.presentation.common.AppWideEventDelegate
import com.pavlushinsa.recipescompapp.presentation.common.mapper.toUiErrorType
import com.pavlushinsa.recipescompapp.presentation.common.model.UiErrorType
import com.pavlushinsa.recipescompapp.presentation.common.model.UiEvent
import com.pavlushinsa.recipescompapp.presentation.common.navigation.Destination
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
class RecipesListViewModelTest : BaseViewModelTest() {

    @RelaxedMockK
    private lateinit var getCategoryWithRecipesUseCase: GetCategoryWithRecipesUseCase

    @RelaxedMockK
    private lateinit var syncRecipesForCategoryUseCase: SyncRecipesForCategoryUseCase

    @RelaxedMockK
    private lateinit var eventDelegate: AppWideEventDelegate

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: RecipesListViewModel
    private val testCategoryId = 1

    @Before
    override fun setUp() {
        super.setUp()
        MockKAnnotations.init(this)
        savedStateHandle = SavedStateHandle(mapOf(Destination.CATEGORY_ID to testCategoryId))
    }

    // region Initialization and Data Loading

    @Test
    fun `GIVEN valid ID and fresh cache WHEN initialized THEN should load data without triggering sync`() =
        runTest {
            // ARRANGE
            val freshData = CategoryWithRecipes(
                category = Category(
                    id = testCategoryId,
                    title = "Fresh Category",
                    description = "Category description",
                    imageUrl = "image_url",
                    lastSyncTime = System.currentTimeMillis()
                ),
                recipes = listOf(
                    Recipe(
                        id = 101, categoryId = testCategoryId, title = "Recipe 1",
                        method = emptyList(), imageUrl = "recipe_url", isFavorite = false,
                        lastSyncTime = System.currentTimeMillis()
                    )
                )
            )
            coEvery { getCategoryWithRecipesUseCase(testCategoryId) } returns flowOf(freshData)

            // ACT
            viewModel = RecipesListViewModel(
                getCategoryWithRecipesUseCase,
                syncRecipesForCategoryUseCase,
                eventDelegate,
                savedStateHandle
            )

            // ASSERT
            viewModel.recipesListUiState.test {
                awaitItem() // initial state
                awaitItem() // data loaded

                val finalState = awaitItem()
                assertEquals(false, finalState.isLoading)
                assertEquals("Fresh Category", finalState.categoryTitle)
                assertEquals(1, finalState.recipes.size)
                assertEquals("Recipe 1", finalState.recipes[0].title)

                expectNoEvents()
            }

            advanceUntilIdle()

            // VERIFY
            coVerify(exactly = 0) { syncRecipesForCategoryUseCase(any()) }
        }

    @Test
    fun `GIVEN valid ID and stale cache WHEN initialized THEN should load data and trigger initial sync`() =
        runTest {
            // ARRANGE
            val staleData = CategoryWithRecipes(
                category = Category(
                    id = testCategoryId,
                    title = "Stale Category",
                    description = "Old description",
                    imageUrl = "image_url",
                    lastSyncTime = 0L
                ),
                recipes = listOf(
                    Recipe(
                        id = 101, categoryId = testCategoryId, title = "Recipe 1",
                        method = emptyList(), imageUrl = "recipe_url", isFavorite = false,
                        lastSyncTime = 0L
                    )
                )
            )
            coEvery { getCategoryWithRecipesUseCase(testCategoryId) } returns flowOf(staleData)
            coEvery { syncRecipesForCategoryUseCase(testCategoryId) } returns DataResult.Success(
                Unit
            )

            // ACT
            viewModel = RecipesListViewModel(
                getCategoryWithRecipesUseCase,
                syncRecipesForCategoryUseCase,
                eventDelegate,
                savedStateHandle
            )

            // ASSERT
            viewModel.recipesListUiState.test {
                awaitItem() // initial state

                val dataLoadedState = awaitItem()
                assertEquals(true, dataLoadedState.isLoading)
                assertEquals("Stale Category", dataLoadedState.categoryTitle)
                assertEquals(1, dataLoadedState.recipes.size)

                val finalState = awaitItem()
                assertEquals(false, finalState.isLoading)
                assertEquals("Stale Category", finalState.categoryTitle)

                cancelAndIgnoreRemainingEvents()
            }

            advanceUntilIdle()

            // VERIFY
            coVerify(atLeast = 1) { getCategoryWithRecipesUseCase(testCategoryId) }
            coVerify(exactly = 1) { syncRecipesForCategoryUseCase(testCategoryId) }
        }

    @Test
    fun `GIVEN valid ID and empty cache WHEN initialized THEN should trigger sync`() = runTest {
        // ARRANGE
        coEvery { getCategoryWithRecipesUseCase(testCategoryId) } returns flowOf(null)
        coEvery { syncRecipesForCategoryUseCase(testCategoryId) } returns DataResult.Success(Unit)

        // ACT
        viewModel = RecipesListViewModel(
            getCategoryWithRecipesUseCase,
            syncRecipesForCategoryUseCase,
            eventDelegate,
            savedStateHandle
        )

        // ASSERT
        viewModel.recipesListUiState.test {
            assertEquals(true, awaitItem().isLoading)
            assertEquals(false, awaitItem().isLoading)
        }

        advanceUntilIdle()

        // VERIFY
        coVerify(atLeast = 1) { getCategoryWithRecipesUseCase(testCategoryId) }
        coVerify(exactly = 1) { syncRecipesForCategoryUseCase(testCategoryId) }
    }

    @Test
    fun `GIVEN valid ID and category with no recipes WHEN initialized THEN should trigger sync`() =
        runTest {
            // ARRANGE
            val emptyCategoryData = CategoryWithRecipes(
                category = Category(
                    id = testCategoryId,
                    title = "Empty Category",
                    description = "",
                    imageUrl = "image_url",
                    lastSyncTime = System.currentTimeMillis()
                ),
                recipes = emptyList()
            )
            coEvery { getCategoryWithRecipesUseCase(testCategoryId) } returns flowOf(
                emptyCategoryData
            )
            coEvery { syncRecipesForCategoryUseCase(testCategoryId) } returns DataResult.Success(
                Unit
            )

            // ACT
            viewModel = RecipesListViewModel(
                getCategoryWithRecipesUseCase,
                syncRecipesForCategoryUseCase,
                eventDelegate,
                savedStateHandle
            )

            // ASSERT
            viewModel.recipesListUiState.test {
                awaitItem() // initial state

                val dataLoadedState = awaitItem()
                assertEquals(true, dataLoadedState.isLoading)
                assertEquals("Empty Category", dataLoadedState.categoryTitle)
                assertEquals(true, dataLoadedState.recipes.isEmpty())

                val finalState = awaitItem()
                assertEquals(false, finalState.isLoading)

                cancelAndIgnoreRemainingEvents()
            }

            advanceUntilIdle()

            // VERIFY
            coVerify(atLeast = 1) { getCategoryWithRecipesUseCase(testCategoryId) }
            coVerify(exactly = 1) { syncRecipesForCategoryUseCase(testCategoryId) }
        }

    @Test
    fun `GIVEN invalid ID WHEN initialized THEN should not load data and send error event`() =
        runTest {
            // ARRANGE
            val invalidId = Destination.INVALID_ID
            savedStateHandle = SavedStateHandle(mapOf(Destination.CATEGORY_ID to invalidId))
            val eventSlot = slot<UiEvent>()

            // ACT
            viewModel = RecipesListViewModel(
                getCategoryWithRecipesUseCase,
                syncRecipesForCategoryUseCase,
                eventDelegate,
                savedStateHandle
            )

            // ASSERT
            viewModel.recipesListUiState.test {
                val state = awaitItem()
                assertEquals(false, state.isLoading)
                assertEquals(true, state.recipes.isEmpty())
                assertEquals("", state.categoryTitle)
                expectNoEvents()
            }

            // VERIFY
            coVerify(exactly = 1) { eventDelegate.sendAppWideEvent(capture(eventSlot)) }
            val capturedEvent = eventSlot.captured
            assert(capturedEvent is UiEvent.ShowSnackBarEvent)
            assertEquals(
                UiErrorType.Unknown,
                (capturedEvent as UiEvent.ShowSnackBarEvent).errorType
            )

            coVerify(exactly = 0) { getCategoryWithRecipesUseCase(any()) }
            coVerify(exactly = 0) { syncRecipesForCategoryUseCase(any()) }
        }

    @Test
    fun `GIVEN initial sync fails WHEN initialized THEN should show cached data and send error event`() =
        runTest {
            // ARRANGE
            val staleData = CategoryWithRecipes(
                category = Category(
                    id = testCategoryId, title = "Stale Category", description = "",
                    imageUrl = "image_url", lastSyncTime = 0L
                ),
                recipes = listOf(
                    Recipe(
                        id = 101, categoryId = testCategoryId, title = "Recipe 1",
                        method = emptyList(), imageUrl = "recipe_url", isFavorite = false,
                        lastSyncTime = 0L
                    )
                )
            )
            val testError = Error.NoInternetConnection
            val eventSlot = slot<UiEvent>()

            coEvery { getCategoryWithRecipesUseCase(testCategoryId) } returns flowOf(staleData)
            coEvery { syncRecipesForCategoryUseCase(testCategoryId) } returns DataResult.Failure(
                testError
            )

            // ACT
            viewModel = RecipesListViewModel(
                getCategoryWithRecipesUseCase,
                syncRecipesForCategoryUseCase,
                eventDelegate,
                savedStateHandle
            )

            // ASSERT
            viewModel.recipesListUiState.test {
                awaitItem() // initial
                awaitItem() // data loaded

                val finalState = awaitItem()
                assertEquals(false, finalState.isLoading)
                assertEquals("Stale Category", finalState.categoryTitle)
                assertEquals(1, finalState.recipes.size)

                cancelAndIgnoreRemainingEvents()
            }

            advanceUntilIdle()

            // VERIFY
            coVerify(exactly = 1) { syncRecipesForCategoryUseCase(testCategoryId) }

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
            val testException = SQLiteException("DB Read Error")
            val eventSlot = slot<UiEvent>()
            coEvery { getCategoryWithRecipesUseCase(testCategoryId) } returns flow { throw testException }

            // ACT
            viewModel = RecipesListViewModel(
                getCategoryWithRecipesUseCase,
                syncRecipesForCategoryUseCase,
                eventDelegate,
                savedStateHandle
            )

            advanceUntilIdle()

            // ASSERT
            viewModel.recipesListUiState.test {
                val state = awaitItem()
                assertEquals(false, state.isLoading)
                assertEquals(true, state.recipes.isEmpty())
            }

            // VERIFY
            coVerify(exactly = 1) { eventDelegate.sendAppWideEvent(capture(eventSlot)) }
            val capturedEvent = eventSlot.captured
            assert(capturedEvent is UiEvent.ShowSnackBarEvent)
            assertEquals(
                UiErrorType.Database,
                (capturedEvent as UiEvent.ShowSnackBarEvent).errorType
            )

            coVerify(exactly = 0) { syncRecipesForCategoryUseCase(any()) }
        }

    // endregion

    // region Pull-to-Refresh

    @Test
    fun `GIVEN user requests refresh WHEN sync is successful THEN should show refreshing state and update data`() =
        runTest {
            // ARRANGE
            val initialData = CategoryWithRecipes(
                category = Category(
                    id = testCategoryId, title = "Initial", description = "",
                    imageUrl = "image_url", lastSyncTime = System.currentTimeMillis()
                ),
                recipes = emptyList()
            )
            coEvery { getCategoryWithRecipesUseCase(testCategoryId) } returns flowOf(initialData)
            coEvery { syncRecipesForCategoryUseCase(testCategoryId) } returns DataResult.Success(
                Unit
            )

            viewModel = RecipesListViewModel(
                getCategoryWithRecipesUseCase,
                syncRecipesForCategoryUseCase,
                eventDelegate,
                savedStateHandle
            )
            advanceUntilIdle()

            viewModel.recipesListUiState.test {
                awaitItem()

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
            coVerify(exactly = 2) { syncRecipesForCategoryUseCase(testCategoryId) }
        }

    @Test
    fun `GIVEN user requests refresh WHEN sync fails THEN should show refreshing state and send error event`() =
        runTest {
            // ARRANGE
            val initialData = CategoryWithRecipes(
                category = Category(
                    id = testCategoryId, title = "Initial", description = "",
                    imageUrl = "image_url", lastSyncTime = System.currentTimeMillis()
                ),
                recipes = emptyList()
            )
            val testError = Error.NoInternetConnection
            val eventList = mutableListOf<UiEvent>()

            coEvery { getCategoryWithRecipesUseCase(testCategoryId) } returns flowOf(initialData)
            coEvery { syncRecipesForCategoryUseCase(testCategoryId) } returns DataResult.Failure(
                testError
            )

            viewModel = RecipesListViewModel(
                getCategoryWithRecipesUseCase,
                syncRecipesForCategoryUseCase,
                eventDelegate,
                savedStateHandle
            )
            advanceUntilIdle()

            viewModel.recipesListUiState.test {
                awaitItem()

                // ACT
                viewModel.onRefresh()

                // ASSERT
                assertEquals(true, awaitItem().isRefreshing)
                assertEquals(false, awaitItem().isRefreshing)

                cancelAndIgnoreRemainingEvents()
            }

            // VERIFY
            coVerify(exactly = 2) { syncRecipesForCategoryUseCase(testCategoryId) }
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
