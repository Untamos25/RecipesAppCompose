package com.example.composeapp.presentation.recipes.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.recipes.usecase.CalculateIngredientsUseCase
import com.example.composeapp.domain.recipes.usecase.GetRecipeDetailsUseCase
import com.example.composeapp.domain.recipes.usecase.UpdateFavoriteStatusUseCase
import com.example.composeapp.presentation.common.constants.SliderConstants
import com.example.composeapp.presentation.common.constants.UIConstants.FLOW_SUBSCRIPTION_TIMEOUT
import com.example.composeapp.presentation.common.navigation.Destination
import com.example.composeapp.presentation.recipes.detail.mapper.toIngredientUiModel
import com.example.composeapp.presentation.recipes.detail.mapper.toRecipeDetailsUiModel
import com.example.composeapp.presentation.recipes.detail.model.RecipeDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    getRecipeDetailsUseCase: GetRecipeDetailsUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
    private val calculateIngredientsUseCase: CalculateIngredientsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val recipeId: Int = savedStateHandle.get<Int>(Destination.RECIPE_ID) ?: Destination.INVALID_ID

    private val portionsCountFlow = MutableStateFlow(SliderConstants.INITIAL_PORTIONS)
    private val recipeWithIngredientsFlow = getRecipeDetailsUseCase(recipeId)

    val recipeDetailsUiState: StateFlow<RecipeDetailsUiState> =
        combine(
            flow = recipeWithIngredientsFlow,
            flow2 = portionsCountFlow
        ) { recipeWithIngredients, portionsCount ->

            if (recipeWithIngredients == null) {
                RecipeDetailsUiState(isError = true)
            } else {
                val updatedIngredients = calculateIngredientsUseCase(
                    originalIngredients = recipeWithIngredients.ingredients,
                    initialPortions = SliderConstants.INITIAL_PORTIONS,
                    newPortions = portionsCount
                ).map { it.toIngredientUiModel() }.toImmutableList()

                val recipe = recipeWithIngredients.toRecipeDetailsUiModel()
                    .copy(ingredients = updatedIngredients)

                RecipeDetailsUiState(
                    recipe = recipe,
                    portionsCount = portionsCount,
                )
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT),
                initialValue = RecipeDetailsUiState()
            )

    fun onPortionsChanged(newPortions: Float) {
        portionsCountFlow.value = newPortions
    }

    fun onFavoriteClick() {
        val currentRecipe = recipeDetailsUiState.value.recipe ?: return
        viewModelScope.launch {
            updateFavoriteStatusUseCase(currentRecipe.id, !currentRecipe.isFavorite)
        }
    }
}
