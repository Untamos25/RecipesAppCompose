package com.pavlushinsa.recipescompapp.presentation.recipes.detail

import android.database.sqlite.SQLiteException
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.pavlushinsa.recipescompapp.util.BaseViewModelTest
import com.pavlushinsa.recipescompapp.domain.common.DataResult
import com.pavlushinsa.recipescompapp.domain.common.Error
import com.pavlushinsa.recipescompapp.domain.recipes.model.Ingredient
import com.pavlushinsa.recipescompapp.domain.recipes.model.Recipe
import com.pavlushinsa.recipescompapp.domain.recipes.model.RecipeWithIngredients
import com.pavlushinsa.recipescompapp.domain.recipes.usecase.CalculateIngredientsUseCase
import com.pavlushinsa.recipescompapp.domain.recipes.usecase.GetRecipeDetailsUseCase
import com.pavlushinsa.recipescompapp.domain.recipes.usecase.SyncRecipeDetailsUseCase
import com.pavlushinsa.recipescompapp.domain.recipes.usecase.UpdateFavoriteStatusUseCase
import com.pavlushinsa.recipescompapp.presentation.common.AppWideEventDelegate
import com.pavlushinsa.recipescompapp.presentation.common.mapper.toUiErrorType
import com.pavlushinsa.recipescompapp.presentation.common.model.UiErrorType
import com.pavlushinsa.recipescompapp.presentation.common.model.UiEvent
import com.pavlushinsa.recipescompapp.presentation.common.navigation.Destination
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.mapper.toIngredientUiModel
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.mapper.toRecipeDetailsUiModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RecipeDetailsViewModelTest : BaseViewModelTest() {

    @RelaxedMockK
    private lateinit var getRecipeDetailsUseCase: GetRecipeDetailsUseCase

    @RelaxedMockK
    private lateinit var updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase

    @RelaxedMockK
    private lateinit var calculateIngredientsUseCase: CalculateIngredientsUseCase

    @RelaxedMockK
    private lateinit var syncRecipeDetailsUseCase: SyncRecipeDetailsUseCase

    @RelaxedMockK
    private lateinit var eventDelegate: AppWideEventDelegate

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: RecipeDetailsViewModel
    private val testRecipeId = 1

    @Before
    override fun setUp() {
        super.setUp()
        MockKAnnotations.init(this)
        savedStateHandle = SavedStateHandle(mapOf(Destination.RECIPE_ID to testRecipeId))
    }

    // region Initialization and Data Loading

    @Test
    fun `GIVEN valid ID and stale cache WHEN initialized THEN should load data and trigger initial sync`() =
        runTest {
            // ARRANGE
            val testRecipeWithIngredients = RecipeWithIngredients(
                recipe = Recipe(
                    id = testRecipeId, categoryId = 1, title = "Test Recipe",
                    method = listOf("Step 1"), imageUrl = "", isFavorite = false,
                    lastSyncTime = 0L
                ),
                ingredients = listOf(Ingredient("1", "шт", "Test Ingredient"))
            )

            coEvery { getRecipeDetailsUseCase(testRecipeId) } returns flowOf(
                testRecipeWithIngredients
            )
            coEvery { syncRecipeDetailsUseCase(testRecipeId) } returns DataResult.Success(Unit)

            // ACT
            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )

            //ASSERT
            viewModel.recipeDetailsUiState.test {
                val initialState = awaitItem()
                assertEquals(true, initialState.isLoading)
                assertEquals(null, initialState.recipe)

                advanceUntilIdle()

                val dataLoadedState = awaitItem()
                assertEquals(true, dataLoadedState.isLoading)
                assertEquals(
                    testRecipeWithIngredients.toRecipeDetailsUiModel(),
                    dataLoadedState.recipe
                )

                val finalState = awaitItem()
                assertEquals(false, finalState.isLoading)
                assertEquals(testRecipeWithIngredients.toRecipeDetailsUiModel(), finalState.recipe)

                cancelAndIgnoreRemainingEvents()
            }

            // VERIFY
            coVerify(atLeast = 1) { getRecipeDetailsUseCase(testRecipeId) }
            coVerify(exactly = 1) { syncRecipeDetailsUseCase(testRecipeId) }
        }

    @Test
    fun `GIVEN valid ID and empty cache WHEN initialized THEN should trigger sync`() =
        runTest {
            // ARRANGE
            coEvery { getRecipeDetailsUseCase(testRecipeId) } returns flowOf(null)
            coEvery { syncRecipeDetailsUseCase(testRecipeId) } returns DataResult.Success(Unit)

            // ACT
            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )

            // ASSERT
            viewModel.recipeDetailsUiState.test {
                assertEquals(true, awaitItem().isLoading)
                assertEquals(false, awaitItem().isLoading)
            }

            advanceUntilIdle()

            // VERIFY
            coVerify(atLeast = 1) { getRecipeDetailsUseCase(testRecipeId) }
            coVerify(exactly = 1) { syncRecipeDetailsUseCase(testRecipeId) }
        }

    @Test
    fun `GIVEN valid ID and fresh cache WHEN initialized THEN should load data without triggering sync`() =
        runTest {
            // ARRANGE
            val freshData = RecipeWithIngredients(
                recipe = Recipe(
                    id = testRecipeId, categoryId = 1, title = "Fresh Recipe",
                    method = listOf("Step 1"), imageUrl = "", isFavorite = false,
                    lastSyncTime = System.currentTimeMillis()
                ),
                ingredients = emptyList()
            )

            coEvery { getRecipeDetailsUseCase(testRecipeId) } returns flowOf(freshData)

            // ACT
            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )

            // ASSERT
            viewModel.recipeDetailsUiState.test {
                awaitItem() // initial state
                awaitItem() // data loaded

                val finalState = awaitItem()
                assertEquals(false, finalState.isLoading)
                assertEquals("Fresh Recipe", finalState.recipe?.title)

                expectNoEvents()
            }

            advanceUntilIdle()

            // VERIFY
            coVerify(exactly = 0) { syncRecipeDetailsUseCase(any()) }
        }

    @Test
    fun `GIVEN valid ID, fresh cache, but recipe has no method WHEN initialized THEN should trigger sync`() =
        runTest {
            // ARRANGE
            val freshDataWithoutMethod = RecipeWithIngredients(
                recipe = Recipe(
                    id = testRecipeId,
                    categoryId = 1,
                    title = "Recipe Without Method",
                    method = emptyList(),
                    imageUrl = "",
                    isFavorite = false,
                    lastSyncTime = System.currentTimeMillis()
                ),
                ingredients = listOf(Ingredient("1", "шт", "Test Ingredient"))
            )

            coEvery { getRecipeDetailsUseCase(testRecipeId) } returns flowOf(freshDataWithoutMethod)
            coEvery { syncRecipeDetailsUseCase(testRecipeId) } returns DataResult.Success(Unit)

            // ACT
            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )

            // ASSERT
            viewModel.recipeDetailsUiState.test {
                awaitItem() // initial state

                val dataLoadedState = awaitItem()
                assertEquals("Recipe Without Method", dataLoadedState.recipe?.title)
                assertEquals(true, dataLoadedState.recipe?.method?.isEmpty())

                val finalState = awaitItem()
                assertEquals(false, finalState.isLoading)

                cancelAndIgnoreRemainingEvents()
            }

            advanceUntilIdle()

            // VERIFY
            coVerify(exactly = 1) { syncRecipeDetailsUseCase(testRecipeId) }
        }

    @Test
    fun `GIVEN invalid ID WHEN initialized THEN should not load data and send error event`() =
        runTest {
            // ARRANGE
            val invalidId = Destination.INVALID_ID
            savedStateHandle = SavedStateHandle(mapOf(Destination.RECIPE_ID to invalidId))

            val eventSlot = slot<UiEvent>()

            // ACT
            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )

            // ASSERT
            viewModel.recipeDetailsUiState.test {
                val finalState = awaitItem()

                assertEquals(false, finalState.isLoading)
                assertEquals(null, finalState.recipe)

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

            coVerify(exactly = 0) { getRecipeDetailsUseCase(any()) }
            coVerify(exactly = 0) { syncRecipeDetailsUseCase(any()) }
        }

    @Test
    fun `GIVEN initial sync fails WHEN initialized THEN should show cached data and send error event`() =
        runTest {
            // ARRANGE
            val testRecipeWithIngredients = RecipeWithIngredients(
                recipe = Recipe(
                    id = testRecipeId, categoryId = 1, title = "Test Recipe",
                    method = listOf("Step 1"), imageUrl = "", isFavorite = false,
                    lastSyncTime = 0L
                ),
                ingredients = listOf(Ingredient("1", "шт", "Test Ingredient"))
            )
            val testError = Error.NoInternetConnection
            val eventSlot = slot<UiEvent>()

            coEvery { getRecipeDetailsUseCase(testRecipeId) } returns flowOf(
                testRecipeWithIngredients
            )
            coEvery { syncRecipeDetailsUseCase(testRecipeId) } returns DataResult.Failure(testError)

            // ACT
            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )

            // ASSERT
            viewModel.recipeDetailsUiState.test {
                awaitItem() // initial state
                awaitItem() // data loaded state

                val finalState = awaitItem()
                assertEquals(false, finalState.isLoading)
                assertEquals(testRecipeWithIngredients.toRecipeDetailsUiModel(), finalState.recipe)

                cancelAndIgnoreRemainingEvents()
            }

            advanceUntilIdle()

            // VERIFY
            coVerify(exactly = 1) { syncRecipeDetailsUseCase(testRecipeId) }

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
            coEvery { getRecipeDetailsUseCase(testRecipeId) } returns flow { throw testException }

            val eventSlot = slot<UiEvent>()

            // ACT
            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )

            advanceUntilIdle()

            // ASSERT
            viewModel.recipeDetailsUiState.test {
                val finalState = awaitItem()
                assertEquals(false, finalState.isLoading)
                assertEquals(null, finalState.recipe)
            }

            // VERIFY
            coVerify(exactly = 1) { eventDelegate.sendAppWideEvent(capture(eventSlot)) }
            val capturedEvent = eventSlot.captured
            assert(capturedEvent is UiEvent.ShowSnackBarEvent)
            assertEquals(
                UiErrorType.Database,
                (capturedEvent as UiEvent.ShowSnackBarEvent).errorType
            )

            coVerify(exactly = 0) { syncRecipeDetailsUseCase(any()) }
        }

    // endregion

    // region Pull-to-Refresh

    @Test
    fun `GIVEN user requests refresh WHEN sync is successful THEN should show refreshing state and update data`() =
        runTest {
            // ARRANGE
            val initialData = RecipeWithIngredients(
                recipe = Recipe(testRecipeId, 1, "Initial Recipe", emptyList(), "", false, 0L),
                ingredients = emptyList()
            )
            coEvery { getRecipeDetailsUseCase(testRecipeId) } returns flowOf(initialData)
            coEvery { syncRecipeDetailsUseCase(testRecipeId) } returns DataResult.Success(Unit)

            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )
            advanceUntilIdle()

            viewModel.recipeDetailsUiState.test {
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
            coVerify(exactly = 2) { syncRecipeDetailsUseCase(testRecipeId) }
        }

    @Test
    fun `GIVEN user requests refresh WHEN sync fails THEN should show refreshing state and send error event`() =
        runTest {
            // ARRANGE
            val initialData = RecipeWithIngredients(
                recipe = Recipe(testRecipeId, 1, "Initial Recipe", emptyList(), "", false, 0L),
                ingredients = emptyList()
            )
            val testError = Error.NoInternetConnection
            val eventList = mutableListOf<UiEvent>()

            coEvery { getRecipeDetailsUseCase(testRecipeId) } returns flowOf(initialData)
            coEvery { syncRecipeDetailsUseCase(testRecipeId) } returns DataResult.Failure(testError)

            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )
            advanceUntilIdle()

            viewModel.recipeDetailsUiState.test {
                awaitItem()

                // ACT
                viewModel.onRefresh()

                // ASSERT
                assertEquals(true, awaitItem().isRefreshing)
                assertEquals(false, awaitItem().isRefreshing)

                cancelAndIgnoreRemainingEvents()
            }

            // VERIFY
            coVerify(exactly = 2) { syncRecipeDetailsUseCase(testRecipeId) }

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

    // region Favorites Management

    @Test
    fun `GIVEN recipe is not favorite WHEN onFavoriteClick is called THEN should mark as favorite`() =
        runTest {
            // ARRANGE
            val testRecipe = Recipe(
                id = testRecipeId, categoryId = 1, title = "Test Recipe",
                method = listOf("Step 1"), imageUrl = "", isFavorite = false,
                lastSyncTime = System.currentTimeMillis()
            )
            val recipeWithIngredients = RecipeWithIngredients(testRecipe, emptyList())
            coEvery { getRecipeDetailsUseCase(testRecipeId) } returns flowOf(recipeWithIngredients)

            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )
            advanceUntilIdle()

            // ACT
            viewModel.onFavoriteClick()

            advanceUntilIdle()

            // VERIFY
            coVerify(exactly = 1) {
                updateFavoriteStatusUseCase(
                    recipeId = testRecipeId,
                    isFavorite = true
                )
            }
        }

    @Test
    fun `GIVEN recipe is favorite WHEN onFavoriteClick is called THEN should unmark as favorite`() =
        runTest {
            // ARRANGE
            val testRecipe = Recipe(
                id = testRecipeId, categoryId = 1, title = "Favorite Recipe",
                method = listOf("Step 1"), imageUrl = "", isFavorite = true,
                lastSyncTime = System.currentTimeMillis()
            )
            val recipeWithIngredients = RecipeWithIngredients(testRecipe, emptyList())
            coEvery { getRecipeDetailsUseCase(testRecipeId) } returns flowOf(recipeWithIngredients)

            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )
            advanceUntilIdle()

            // ACT
            viewModel.onFavoriteClick()

            advanceUntilIdle()

            // VERIFY
            coVerify(exactly = 1) {
                updateFavoriteStatusUseCase(
                    recipeId = testRecipeId,
                    isFavorite = false
                )
            }
        }

    @Test
    fun `GIVEN database error on favorite update WHEN onFavoriteClick is called THEN should send error event`() =
        runTest {
            // ARRANGE
            val testRecipe = Recipe(
                id = testRecipeId, categoryId = 1, title = "Test Recipe",
                method = listOf("Step 1"), imageUrl = "", isFavorite = false,
                lastSyncTime = System.currentTimeMillis()
            )
            val recipeWithIngredients = RecipeWithIngredients(testRecipe, emptyList())
            val eventSlot = slot<UiEvent>()

            coEvery { getRecipeDetailsUseCase(testRecipeId) } returns flowOf(recipeWithIngredients)
            coEvery { updateFavoriteStatusUseCase(any(), any()) } throws SQLiteException("DB Error")

            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )
            advanceUntilIdle()

            // ACT
            viewModel.onFavoriteClick()

            advanceUntilIdle()

            // VERIFY
            coVerify(exactly = 1) { eventDelegate.sendAppWideEvent(capture(eventSlot)) }
            val capturedEvent = eventSlot.captured
            assert(capturedEvent is UiEvent.ShowSnackBarEvent)
            assertEquals(
                UiErrorType.Database,
                (capturedEvent as UiEvent.ShowSnackBarEvent).errorType
            )
        }

    // endregion

    // region Portion Calculation

    @Test
    fun `GIVEN data is loaded WHEN portions are changed THEN should recalculate ingredients`() =
        runTest {
            // ARRANGE
            val originalIngredients = listOf(
                Ingredient("100", "г", "Мука"),
                Ingredient("2.0", "шт", "Яйцо"),
                Ingredient("", "по вкусу", "Соль")
            )
            val initialRecipe = RecipeWithIngredients(
                recipe = Recipe(
                    id = testRecipeId, categoryId = 1, title = "Test Recipe",
                    method = listOf("Step 1"), imageUrl = "", isFavorite = false,
                    lastSyncTime = System.currentTimeMillis()
                ),
                ingredients = originalIngredients
            )
            val newPortions = 2.0f

            val calculatedIngredients = listOf(
                Ingredient("200.0", "г", "Мука"),
                Ingredient("4.0", "шт", "Яйцо"),
                Ingredient("", "по вкусу", "Соль")
            )
            val calculatedIngredientsUi =
                calculatedIngredients.map { it.toIngredientUiModel() }.toPersistentList()

            coEvery { getRecipeDetailsUseCase(testRecipeId) } returns flowOf(initialRecipe)
            coEvery {
                calculateIngredientsUseCase(
                    originalIngredients = originalIngredients,
                    initialPortions = 1f,
                    newPortions = newPortions
                )
            } returns calculatedIngredients

            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )
            advanceUntilIdle()

            // ASSERT
            viewModel.recipeDetailsUiState.test {
                awaitItem()

                // ACT
                viewModel.onPortionsChange(newPortions)

                // ASSERT
                val intermediateState = awaitItem()
                assertEquals(newPortions, intermediateState.portionsCount)
                assertEquals(
                    originalIngredients.map { it.toIngredientUiModel() }.toPersistentList(),
                    intermediateState.recipe?.ingredients
                )

                val finalState = awaitItem()
                assertEquals(newPortions, finalState.portionsCount)
                assertEquals(calculatedIngredientsUi, finalState.recipe?.ingredients)

                expectNoEvents()
            }

            // VERIFY
            coVerify(exactly = 1) {
                calculateIngredientsUseCase(
                    originalIngredients = originalIngredients,
                    initialPortions = 1f,
                    newPortions = newPortions
                )
            }
        }

// RecipeDetailsViewModelTest.kt

    @Test
    fun `GIVEN data is not loaded WHEN portions are changed THEN should do nothing`() =
        runTest {
            // ARRANGE
            coEvery { getRecipeDetailsUseCase(testRecipeId) } returns flowOf(null)
            coEvery { syncRecipeDetailsUseCase(testRecipeId) } returns DataResult.Success(Unit)

            viewModel = RecipeDetailsViewModel(
                getRecipeDetailsUseCase, updateFavoriteStatusUseCase, calculateIngredientsUseCase,
                syncRecipeDetailsUseCase, eventDelegate, savedStateHandle
            )

            advanceUntilIdle()

            // ACT
            viewModel.onPortionsChange(3.0f)

            // ASSERT
            assertEquals(null, viewModel.recipeDetailsUiState.value.recipe)
            assertEquals(1f, viewModel.recipeDetailsUiState.value.portionsCount)

            // VERIFY
            coVerify(exactly = 0) { calculateIngredientsUseCase(any(), any(), any()) }
        }

    // endregion
}
