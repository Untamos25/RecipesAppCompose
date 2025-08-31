package com.example.composeapp.presentation.recipes.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.recipes.usecase.CalculateIngredientsUseCase
import com.example.composeapp.domain.recipes.usecase.GetRecipeDetailsUseCase
import com.example.composeapp.domain.recipes.usecase.SyncRecipeDetailsUseCase
import com.example.composeapp.domain.recipes.usecase.UpdateFavoriteStatusUseCase
import com.example.composeapp.presentation.common.constants.CacheConstants.CACHE_LIFETIME_MS
import com.example.composeapp.presentation.common.constants.SliderConstants.INITIAL_PORTIONS
import com.example.composeapp.presentation.common.navigation.Destination
import com.example.composeapp.presentation.recipes.detail.mapper.toIngredientUiModel
import com.example.composeapp.presentation.recipes.detail.mapper.toRecipeDetailsUiModel
import com.example.composeapp.presentation.recipes.detail.model.RecipeDetailsUiState
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
class RecipeDetailViewModel @Inject constructor(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
    private val calculateIngredientsUseCase: CalculateIngredientsUseCase,
    private val syncRecipeDetailsUseCase: SyncRecipeDetailsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val recipeId: Int =
        savedStateHandle.get<Int>(Destination.RECIPE_ID) ?: Destination.INVALID_ID

    private val _recipeDetailsUiState = MutableStateFlow(RecipeDetailsUiState())
    val recipeDetailsUiState = _recipeDetailsUiState.asStateFlow()

    init {
        if (recipeId != Destination.INVALID_ID) {
            observeLocalData()
            syncIfRequired()
        } else {
            _recipeDetailsUiState.update { it.copy(isLoading = false, isError = true) }
        }
    }

    private fun observeLocalData() {
        viewModelScope.launch {
            getRecipeDetailsUseCase(recipeId)
                .catch {
                    _recipeDetailsUiState.update { it.copy(isLoading = false, isError = true) }
                }
                .collect { recipeWithIngredients ->
                    if (recipeWithIngredients != null) {
                        val recipe = recipeWithIngredients.toRecipeDetailsUiModel()
                        val ingredients = recipeWithIngredients.ingredients.toPersistentList()

                        _recipeDetailsUiState.update {
                            it.copy(
                                recipe = recipe,
                                ingredients = ingredients
                            )
                        }
                        recalculateIngredients(recipeDetailsUiState.value.portionsCount)
                    } else {
                        _recipeDetailsUiState.update { it.copy(isLoading = false, isError = true) }
                    }
                }
        }
    }

    private fun syncIfRequired() {
        viewModelScope.launch {
            try {
                val recipeDetails = getRecipeDetailsUseCase(recipeId).first()

                if (recipeDetails == null || recipeDetails.recipe.method.isEmpty()) {
                    syncData()
                    return@launch
                }

                val lastSyncTime = recipeDetails.recipe.lastSyncTime
                val isCacheStale = (System.currentTimeMillis() - lastSyncTime) > CACHE_LIFETIME_MS

                if (isCacheStale) {
                    launch { syncData() }
                }
            } finally {
                _recipeDetailsUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _recipeDetailsUiState.update { it.copy(isRefreshing = true) }
            syncData()
            _recipeDetailsUiState.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun syncData() {
        try {
            syncRecipeDetailsUseCase(recipeId)
        } catch (e: Exception) {
            _recipeDetailsUiState.update { it.copy(isError = true) }
            e.printStackTrace()
        }
    }

    fun onPortionsChange(newPortions: Float) {
        _recipeDetailsUiState.update { it.copy(portionsCount = newPortions) }
        recalculateIngredients(newPortions)
    }

    private fun recalculateIngredients(newPortions: Float) {
        val recipe = _recipeDetailsUiState.value.recipe
        val ingredients = _recipeDetailsUiState.value.ingredients
        if (ingredients.isEmpty() || recipe == null) return

        val updatedIngredients = calculateIngredientsUseCase(
            originalIngredients = ingredients,
            initialPortions = INITIAL_PORTIONS,
            newPortions = newPortions
        ).map { it.toIngredientUiModel() }.toPersistentList()
        val updatedRecipe = recipe.copy(ingredients = updatedIngredients)

        _recipeDetailsUiState.update { it.copy(recipe = updatedRecipe) }
    }

    fun onFavoriteClick() {
        val recipe = _recipeDetailsUiState.value.recipe ?: return
        viewModelScope.launch {
            updateFavoriteStatusUseCase(recipe.id, !recipe.isFavorite)
        }
    }
}
