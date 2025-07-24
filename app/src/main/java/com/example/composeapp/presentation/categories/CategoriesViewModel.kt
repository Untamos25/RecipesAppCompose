package com.example.composeapp.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.categories.usecase.GetCategoriesUseCase
import com.example.composeapp.presentation.categories.mapper.toUiModel
import com.example.composeapp.presentation.categories.model.CategoriesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _categoriesUiState = MutableStateFlow(CategoriesUiState())
    val categoriesUiState = _categoriesUiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val categories = getCategoriesUseCase().map { it.toUiModel() }
            _categoriesUiState.update { it.copy(categories = categories) }
        }
    }
}
