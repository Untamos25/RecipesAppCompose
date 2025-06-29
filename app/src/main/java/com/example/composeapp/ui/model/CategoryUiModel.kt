package com.example.composeapp.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class CategoryUiModel(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
)