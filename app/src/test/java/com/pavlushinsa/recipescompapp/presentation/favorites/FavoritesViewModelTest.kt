package com.pavlushinsa.recipescompapp.presentation.favorites

import android.database.sqlite.SQLiteException
import app.cash.turbine.test
import com.pavlushinsa.recipescompapp.domain.recipes.model.Recipe
import com.pavlushinsa.recipescompapp.domain.recipes.usecase.GetFavoriteRecipesUseCase
import com.pavlushinsa.recipescompapp.presentation.common.AppWideEventDelegate
import com.pavlushinsa.recipescompapp.presentation.common.model.UiErrorType
import com.pavlushinsa.recipescompapp.presentation.common.model.UiEvent
import com.pavlushinsa.recipescompapp.presentation.recipes.list.mapper.toRecipeCardUiModel
import com.pavlushinsa.recipescompapp.util.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FavoritesViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @RelaxedMockK
    private lateinit var getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase

    @RelaxedMockK
    private lateinit var eventDelegate: AppWideEventDelegate

    private lateinit var viewModel: FavoritesViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    // region Data Observation

    @Test
    fun `GIVEN use case returns favorite recipes WHEN initialized THEN should update state with recipes`() =
        runTest {
            // ARRANGE
            val favoriteRecipes = listOf(
                Recipe(1, 1, "Fav Recipe 1", emptyList(), "url1", true, 0L),
                Recipe(2, 2, "Fav Recipe 2", emptyList(), "url2", true, 0L)
            )
            coEvery { getFavoriteRecipesUseCase() } returns flowOf(favoriteRecipes)

            // ACT
            viewModel = FavoritesViewModel(getFavoriteRecipesUseCase, eventDelegate)

            // ASSERT
            viewModel.favoritesUiState.test {
                awaitItem()

                val finalState = awaitItem()
                assertEquals(2, finalState.recipes.size)
                assertEquals("Fav Recipe 1", finalState.recipes[0].title)
                assertEquals(favoriteRecipes[1].toRecipeCardUiModel(), finalState.recipes[1])

                cancelAndIgnoreRemainingEvents()
            }

            // VERIFY
            coVerify(exactly = 1) { getFavoriteRecipesUseCase() }
            coVerify(exactly = 0) { eventDelegate.sendAppWideEvent(any()) }
        }

    @Test
    fun `GIVEN use case returns an empty list WHEN initialized THEN state should contain an empty list`() =
        runTest {
            // ARRANGE
            coEvery { getFavoriteRecipesUseCase() } returns flowOf(emptyList())

            // ACT
            viewModel = FavoritesViewModel(getFavoriteRecipesUseCase, eventDelegate)

            // ASSERT
            viewModel.favoritesUiState.test {
                val state = awaitItem()
                assertEquals(true, state.recipes.isEmpty())

                expectNoEvents()
            }

            advanceUntilIdle()

            // VERIFY
            coVerify(exactly = 1) { getFavoriteRecipesUseCase() }
            coVerify(exactly = 0) { eventDelegate.sendAppWideEvent(any()) }
        }

    @Test
    fun `GIVEN use case emits updated data WHEN observing THEN state should reflect updates`() =
        runTest {
            // ARRANGE
            val initialList = listOf(
                Recipe(1, 1, "Recipe A", emptyList(), "urlA", true, 0L)
            )
            val updatedList = listOf(
                Recipe(1, 1, "Recipe A", emptyList(), "urlA", true, 0L),
                Recipe(2, 2, "Recipe B", emptyList(), "urlB", true, 0L)
            )

            val favoritesFlow = MutableStateFlow(initialList)
            coEvery { getFavoriteRecipesUseCase() } returns favoritesFlow

            // ACT
            viewModel = FavoritesViewModel(getFavoriteRecipesUseCase, eventDelegate)

            // ASSERT
            viewModel.favoritesUiState.test {
                awaitItem()

                val firstState = awaitItem()
                assertEquals(1, firstState.recipes.size)
                assertEquals("Recipe A", firstState.recipes.first().title)

                favoritesFlow.value = updatedList

                val secondState = awaitItem()
                assertEquals(2, secondState.recipes.size)
                assertEquals("Recipe B", secondState.recipes.last().title)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `GIVEN database read error WHEN initialized THEN should send error event and state remains empty`() = runTest {
        // ARRANGE
        val testException = SQLiteException("Test DB Error")
        val eventSlot = slot<UiEvent>()

        coEvery { getFavoriteRecipesUseCase() } returns flow { throw testException }

        // ACT
        viewModel = FavoritesViewModel(getFavoriteRecipesUseCase, eventDelegate)

        // ASSERT
        viewModel.favoritesUiState.test {
            val state = awaitItem()
            assertEquals(true, state.recipes.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }

        advanceUntilIdle()

        // VERIFY
        coVerify(exactly = 1) { eventDelegate.sendAppWideEvent(capture(eventSlot)) }
        val capturedEvent = eventSlot.captured
        assert(capturedEvent is UiEvent.ShowSnackBarEvent)
        assertEquals(UiErrorType.Database, (capturedEvent as UiEvent.ShowSnackBarEvent).errorType)
    }

    @Test
    fun `GIVEN an unknown error WHEN initialized THEN should send an unknown error event`() = runTest {
        // ARRANGE
        val testException = RuntimeException("Some unknown error")
        val eventSlot = slot<UiEvent>()

        coEvery { getFavoriteRecipesUseCase() } returns flow { throw testException }

        // ACT
        viewModel = FavoritesViewModel(getFavoriteRecipesUseCase, eventDelegate)

        // ASSERT
        viewModel.favoritesUiState.test {
            val state = awaitItem()
            assertEquals(true, state.recipes.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }

        advanceUntilIdle()

        // VERIFY
        coVerify(exactly = 1) { eventDelegate.sendAppWideEvent(capture(eventSlot)) }
        val capturedEvent = eventSlot.captured
        assert(capturedEvent is UiEvent.ShowSnackBarEvent)
        assertEquals(UiErrorType.Unknown, (capturedEvent as UiEvent.ShowSnackBarEvent).errorType)
    }

    // endregion

}
