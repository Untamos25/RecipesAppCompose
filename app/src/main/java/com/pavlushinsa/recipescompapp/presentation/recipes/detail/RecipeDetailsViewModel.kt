package com.pavlushinsa.recipescompapp.presentation.recipes.detail

import android.database.sqlite.SQLiteException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavlushinsa.recipescompapp.domain.common.DataResult
import com.pavlushinsa.recipescompapp.domain.common.Error
import com.pavlushinsa.recipescompapp.domain.recipes.model.Ingredient
import com.pavlushinsa.recipescompapp.domain.recipes.usecase.CalculateIngredientsUseCase
import com.pavlushinsa.recipescompapp.domain.recipes.usecase.GetRecipeDetailsUseCase
import com.pavlushinsa.recipescompapp.domain.recipes.usecase.SyncRecipeDetailsUseCase
import com.pavlushinsa.recipescompapp.domain.recipes.usecase.UpdateFavoriteStatusUseCase
import com.pavlushinsa.recipescompapp.presentation.common.AppWideEventDelegate
import com.pavlushinsa.recipescompapp.presentation.common.constants.CacheConstants.CACHE_LIFETIME_MS
import com.pavlushinsa.recipescompapp.presentation.common.mapper.toUiErrorType
import com.pavlushinsa.recipescompapp.presentation.common.model.UiErrorType
import com.pavlushinsa.recipescompapp.presentation.common.model.UiEvent
import com.pavlushinsa.recipescompapp.presentation.common.navigation.Destination
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.mapper.toIngredientUiModel
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.mapper.toRecipeDetailsUiModel
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.model.INITIAL_PORTIONS
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.model.RecipeDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
    private val calculateIngredientsUseCase: CalculateIngredientsUseCase,
    private val syncRecipeDetailsUseCase: SyncRecipeDetailsUseCase,
    private val eventDelegate: AppWideEventDelegate,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), AppWideEventDelegate by eventDelegate {

    private val recipeId: Int =
        savedStateHandle.get<Int>(Destination.RECIPE_ID) ?: Destination.INVALID_ID

    private val _recipeDetailsUiState = MutableStateFlow(RecipeDetailsUiState())
    val recipeDetailsUiState = _recipeDetailsUiState.asStateFlow()

    private var originalIngredients: List<Ingredient> = emptyList()

    init {
        if (recipeId != Destination.INVALID_ID) {
            observeLocalData()
            syncIfRequired()
        } else {
            _recipeDetailsUiState.update { it.copy(isLoading = false) }
            sendAppWideEvent(UiEvent.ShowSnackBarEvent(UiErrorType.Unknown))
        }
    }

    private fun observeLocalData() {
        viewModelScope.launch {
            getRecipeDetailsUseCase(recipeId)
                .catch { throwable ->
                    val error = if (throwable is SQLiteException) Error.DatabaseError
                    else Error.UnknownError
                    sendAppWideEvent(UiEvent.ShowSnackBarEvent(error.toUiErrorType()))
                }
                .collect { recipeWithIngredients ->
                    if (recipeWithIngredients != null) {
                        val recipe = recipeWithIngredients.toRecipeDetailsUiModel()
                        originalIngredients = recipeWithIngredients.ingredients

                        _recipeDetailsUiState.update {
                            it.copy(
                                recipe = recipe,
                            )
                        }
                    } else {
                        if (!_recipeDetailsUiState.value.isLoading) {
                            sendAppWideEvent(UiEvent.ShowSnackBarEvent(UiErrorType.NotFound))
                        }
                    }
                }
        }
    }

    private fun syncIfRequired() {
        viewModelScope.launch {
            try {
                val recipeDetails = getRecipeDetailsUseCase(recipeId).first()

                if (recipeDetails == null || recipeDetails.recipe.method.isEmpty()) {
                    syncData { onRefresh() }
                    return@launch
                }

                val lastSyncTime = recipeDetails.recipe.lastSyncTime
                val isCacheStale = (System.currentTimeMillis() - lastSyncTime) > CACHE_LIFETIME_MS

                if (isCacheStale) {
                    launch { syncData { onRefresh() } }
                }
            } finally {
                _recipeDetailsUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _recipeDetailsUiState.update { it.copy(isRefreshing = true) }
            syncData { onRefresh() }
            _recipeDetailsUiState.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun syncData(onRetry: (() -> Unit)? = null) {
        when (val result = syncRecipeDetailsUseCase(recipeId)) {
            is DataResult.Success -> { /* no-op */ }

            is DataResult.Failure -> {
                sendAppWideEvent(
                    UiEvent.ShowSnackBarEvent(
                        errorType = result.error.toUiErrorType(),
                        onRetry = onRetry
                    )
                )
            }
        }
    }

    fun onPortionsChange(newPortions: Float) {
        _recipeDetailsUiState.update { it.copy(portionsCount = newPortions) }
        recalculateIngredients(newPortions)
    }

    private fun recalculateIngredients(newPortions: Float) {
        val currentRecipe = _recipeDetailsUiState.value.recipe
        if (originalIngredients.isEmpty() || currentRecipe == null) return

        val updatedIngredients = calculateIngredientsUseCase(
            originalIngredients = originalIngredients,
            initialPortions = INITIAL_PORTIONS,
            newPortions = newPortions
        ).map { it.toIngredientUiModel() }.toPersistentList()
        val updatedRecipe = currentRecipe.copy(ingredients = updatedIngredients)

        _recipeDetailsUiState.update { it.copy(recipe = updatedRecipe) }
    }

    fun onFavoriteClick() {
        val recipe = _recipeDetailsUiState.value.recipe ?: return
        viewModelScope.launch {
            updateFavoriteStatusUseCase(recipe.id, !recipe.isFavorite)
        }
    }
}
