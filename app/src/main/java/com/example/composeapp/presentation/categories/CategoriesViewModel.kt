package com.example.composeapp.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.categories.usecase.GetCategoriesUseCase
import com.example.composeapp.domain.categories.usecase.SyncCategoriesUseCase
import com.example.composeapp.presentation.categories.mapper.toUiModel
import com.example.composeapp.presentation.categories.model.CategoriesUiState
import com.example.composeapp.presentation.common.constants.CacheConstants.CACHE_LIFETIME_MS
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
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val syncCategoriesUseCase: SyncCategoriesUseCase
) : ViewModel() {

    private val _categoriesUiState = MutableStateFlow(CategoriesUiState())
    val categoriesUiState = _categoriesUiState.asStateFlow()

    init {
        observeLocalCategories()
        syncIfRequired()
    }

    private fun observeLocalCategories() {
        viewModelScope.launch {
            getCategoriesUseCase()
                .catch {
                    _categoriesUiState.update { it.copy(isError = true) }
                }
                .collect { categories ->
                    _categoriesUiState.update {
                        it.copy(categories = categories.map { it.toUiModel() }.toImmutableList())
                    }
                }
        }
    }

    private fun syncIfRequired() {
        viewModelScope.launch {
            try {
                val categories = getCategoriesUseCase().first()

                if (categories.isEmpty()) {
                    syncData()
                    return@launch
                }

                val oldestSyncTime = categories.minOfOrNull { it.lastSyncTime } ?: 0L
                val isCacheStale = (System.currentTimeMillis() - oldestSyncTime) > CACHE_LIFETIME_MS

                if (isCacheStale) {
                    launch { syncData() }
                }
            } finally {
                _categoriesUiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _categoriesUiState.update { it.copy(isRefreshing = true) }
            syncData()
            _categoriesUiState.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun syncData() {
        try {
            syncCategoriesUseCase()
        } catch (e: Exception) {
            _categoriesUiState.update { it.copy(isError = true) }
            e.printStackTrace()
        }
    }
}
