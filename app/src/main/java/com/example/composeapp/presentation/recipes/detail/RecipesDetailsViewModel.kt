package com.example.composeapp.presentation.recipes.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.recipes.model.Ingredient
import com.example.composeapp.domain.recipes.usecase.CalculateIngredientsUseCase
import com.example.composeapp.domain.recipes.usecase.GetRecipeByIdUseCase
import com.example.composeapp.presentation.common.constants.SliderConstants.INITIAL_PORTIONS
import com.example.composeapp.presentation.common.navigation.Destination.Companion.RECIPE_ID
import com.example.composeapp.presentation.recipes.detail.mapper.toRecipeDetailUiModel
import com.example.composeapp.presentation.recipes.detail.mapper.toUiModelList
import com.example.composeapp.presentation.recipes.detail.model.RecipeDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesDetailViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val calculateIngredientsUseCase: CalculateIngredientsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _recipeDetailUiState = MutableStateFlow(RecipeDetailsUiState())
    val recipeDetailsUiState = _recipeDetailUiState.asStateFlow()

    private var originalIngredients: List<Ingredient> = emptyList()
    private val initialPortions = INITIAL_PORTIONS

    init {
        val recipeId: Int? = savedStateHandle[RECIPE_ID]
        if (recipeId != null) loadRecipe(recipeId)
        else _recipeDetailUiState.update { it.copy(isError = true) }
    }

    private fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val recipeDomain = getRecipeByIdUseCase(recipeId)

            if (recipeDomain != null) {
                originalIngredients = recipeDomain.ingredients

                _recipeDetailUiState.update {
                    it.copy(
                        recipe = recipeDomain.toRecipeDetailUiModel(),
                        portionsCount = initialPortions,
                        calculatedIngredients = originalIngredients
                            .toUiModelList()
                            .toImmutableList()
                    )
                }
            } else {
                _recipeDetailUiState.update { it.copy(isError = true) }
            }
        }
    }

    fun onPortionsChanged(newPosition: Float) {
        val calculatedDomainIngredients = calculateIngredientsUseCase(
            originalIngredients = originalIngredients,
            initialPortions = initialPortions,
            newPortions = newPosition
        )

        _recipeDetailUiState.update {
            it.copy(
                portionsCount = newPosition,
                calculatedIngredients = calculatedDomainIngredients
                    .toUiModelList()
                    .toImmutableList()
            )
        }
    }
}
