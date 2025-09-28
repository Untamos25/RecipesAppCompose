package com.example.composeapp.presentation.recipes.list

import android.database.sqlite.SQLiteException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.categories.usecase.GetCategoryWithRecipesUseCase
import com.example.composeapp.domain.common.DataResult
import com.example.composeapp.domain.common.Error
import com.example.composeapp.domain.recipes.usecase.SyncRecipesForCategoryUseCase
import com.example.composeapp.presentation.categories.mapper.toUiModel
import com.example.composeapp.presentation.common.AppWideEventDelegate
import com.example.composeapp.presentation.common.constants.CacheConstants.CACHE_LIFETIME_MS
import com.example.composeapp.presentation.common.mapper.toUiErrorType
import com.example.composeapp.presentation.common.model.UiErrorType
import com.example.composeapp.presentation.common.model.UiEvent
import com.example.composeapp.presentation.common.navigation.Destination
import com.example.composeapp.presentation.recipes.list.mapper.toRecipeCardUiModel
import com.example.composeapp.presentation.recipes.list.model.RecipesListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val getCategoryWithRecipesUseCase: GetCategoryWithRecipesUseCase,
    private val syncRecipesForCategoryUseCase: SyncRecipesForCategoryUseCase,
    private val eventDelegate: AppWideEventDelegate,
    savedStateHandle: SavedStateHandle
) : ViewModel(), AppWideEventDelegate by eventDelegate {

    private val categoryId: Int =
        savedStateHandle.get<Int>(Destination.CATEGORY_ID) ?: Destination.INVALID_ID

    private val _recipesListUiState = MutableStateFlow(RecipesListUiState())
    val recipesListUiState = _recipesListUiState.asStateFlow()

    init {
        if (categoryId != Destination.INVALID_ID) {
            observeLocalData()
            syncIfRequired()
        } else {
            _recipesListUiState.update { it.copy(isLoading = false) }
            sendAppWideEvent(
                UiEvent.ShowSnackBarEvent(errorType = UiErrorType.Unknown)
            )
        }
    }

    private fun observeLocalData() {
        viewModelScope.launch {
            getCategoryWithRecipesUseCase(categoryId)
                .catch { throwable ->
                    val error =
                        if (throwable is SQLiteException) Error.DatabaseError
                        else Error.UnknownError
                    sendAppWideEvent(UiEvent.ShowSnackBarEvent(error.toUiErrorType()))
                }
                .collect { categoryWithRecipes ->
                    if (categoryWithRecipes != null) {
                        val category = categoryWithRecipes.category.toUiModel()
                        val recipes = categoryWithRecipes.recipes
                            .map { it.toRecipeCardUiModel() }
                            .toImmutableList()

                        _recipesListUiState.update {
                            it.copy(
                                categoryTitle = category.title,
                                categoryImageUrl = if (recipes.isNotEmpty()) category.imageUrl else null,
                                recipes = recipes
                            )
                        }
                    }
                }
        }
    }

    private fun syncIfRequired() {
        viewModelScope.launch {
            try {
                val categoryWithRecipes = getCategoryWithRecipesUseCase(categoryId).first()

                if (categoryWithRecipes == null || categoryWithRecipes.recipes.isEmpty()) {
                    syncData({ onRefresh() })
                    return@launch
                }

                val lastSyncTime = categoryWithRecipes.category.lastSyncTime
                val isCacheStale = (System.currentTimeMillis() - lastSyncTime) > CACHE_LIFETIME_MS

                if (isCacheStale) {
                    launch { syncData() }
                }
            } finally {
                _recipesListUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _recipesListUiState.update { it.copy(isRefreshing = true) }
            syncData{ onRefresh() }
            _recipesListUiState.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun syncData(onRetry: (() -> Unit)? = null) {
        when (val result = syncRecipesForCategoryUseCase(categoryId)) {
            is DataResult.Success -> { /* no-op */
            }

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
}
