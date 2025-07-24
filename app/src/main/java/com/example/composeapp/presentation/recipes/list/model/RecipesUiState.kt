package com.example.composeapp.presentation.recipes.list.model

data class RecipesUiState(
    val categoryTitle: String = "",
    val categoryImageUrl: String = "",
    val recipes: List<RecipeCardUiModel> = emptyList(),
    val isError: Boolean = false
)
