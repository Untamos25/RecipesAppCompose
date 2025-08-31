package com.example.composeapp.presentation.categories.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CategoriesUiState(
    val categories: ImmutableList<CategoryUiModel> = persistentListOf(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val isError: Boolean = false
)
