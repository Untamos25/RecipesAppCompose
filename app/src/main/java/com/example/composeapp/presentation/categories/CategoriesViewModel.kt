package com.example.composeapp.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.categories.usecase.GetCategoriesUseCase
import com.example.composeapp.domain.common.RefreshDataUseCase
import com.example.composeapp.presentation.categories.mapper.toUiModel
import com.example.composeapp.presentation.categories.model.CategoriesUiState
import com.example.composeapp.presentation.common.constants.UIConstants.FLOW_SUBSCRIPTION_TIMEOUT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    getCategoriesUseCase: GetCategoriesUseCase,
    private val refreshDataUseCase: RefreshDataUseCase
) : ViewModel() {

    val categoriesUiState: StateFlow<CategoriesUiState> =
        getCategoriesUseCase()
            .map { categories ->
                CategoriesUiState(
                    categories = categories.map { it.toUiModel() }.toImmutableList()
                )
            }
            .catch {
                emit(CategoriesUiState(isError = true))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT),
                initialValue = CategoriesUiState()
            )

    init {
        refreshData()
    }

    private fun refreshData() {
        viewModelScope.launch {
            refreshDataUseCase()
        }
    }
}
