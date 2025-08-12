package com.example.composeapp.presentation.recipes.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.categories.usecase.GetCategoryWithRecipesUseCase
import com.example.composeapp.presentation.categories.mapper.toUiModel
import com.example.composeapp.presentation.common.constants.UIConstants.FLOW_SUBSCRIPTION_TIMEOUT
import com.example.composeapp.presentation.common.navigation.Destination
import com.example.composeapp.presentation.recipes.list.mapper.toRecipeCardUiModel
import com.example.composeapp.presentation.recipes.list.model.RecipesListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject constructor(
    getCategoryWithRecipesUseCase: GetCategoryWithRecipesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: Int = savedStateHandle.get<Int>(Destination.CATEGORY_ID) ?: Destination.INVALID_ID

    val recipesListUiState: StateFlow<RecipesListUiState> =
        getCategoryWithRecipesUseCase(categoryId)
            .map { categoryWithRecipes ->
                if (categoryWithRecipes == null) {
                    RecipesListUiState(isError = true)
                } else {
                    val category = categoryWithRecipes.category.toUiModel()
                    val recipes =
                        categoryWithRecipes.recipes.map { it.toRecipeCardUiModel() }
                            .toImmutableList()

                    RecipesListUiState(
                        categoryTitle = category.title,
                        categoryImageUrl = category.imageUrl,
                        recipes = recipes
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT),
                initialValue = RecipesListUiState()
            )
}
