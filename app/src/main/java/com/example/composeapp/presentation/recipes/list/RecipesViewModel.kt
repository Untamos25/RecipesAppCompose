package com.example.composeapp.presentation.recipes.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.categories.usecase.GetCategoryByIdUseCase
import com.example.composeapp.domain.recipes.usecase.GetRecipesByCategoryIdUseCase
import com.example.composeapp.presentation.categories.mapper.toUiModel
import com.example.composeapp.presentation.common.navigation.Destination.Companion.CATEGORY_ID
import com.example.composeapp.presentation.recipes.list.mapper.toRecipeCardUiModel
import com.example.composeapp.presentation.recipes.list.model.RecipesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getRecipesByCategoryIdUseCase: GetRecipesByCategoryIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _recipesUiState = MutableStateFlow(RecipesUiState())
    val recipesUiState = _recipesUiState.asStateFlow()

    init {
        val categoryId: Int? = savedStateHandle[CATEGORY_ID]
        if (categoryId != null) loadRecipes(categoryId)
        else _recipesUiState.update { it.copy(isError = true) }
    }

    private fun loadRecipes(categoryId: Int) {
        viewModelScope.launch {
            getCategoryByIdUseCase(categoryId)?.let { categoryDomain ->
                val recipesDomain = getRecipesByCategoryIdUseCase(categoryId)

                val category = categoryDomain.toUiModel()
                val recipes = recipesDomain.map { it.toRecipeCardUiModel() }

                _recipesUiState.update {
                    it.copy(
                        categoryTitle = category.title,
                        categoryImageUrl = category.imageUrl,
                        recipes = recipes
                    )
                }
            } ?: run {
                _recipesUiState.update { it.copy(isError = true) }
            }
        }
    }
}
