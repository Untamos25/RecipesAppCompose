package com.example.composeapp.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.recipes.usecase.GetFavoriteRecipesUseCase
import com.example.composeapp.presentation.common.constants.UIConstants.FLOW_SUBSCRIPTION_TIMEOUT
import com.example.composeapp.presentation.favorites.model.FavoritesUiState
import com.example.composeapp.presentation.recipes.list.mapper.toRecipeCardUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase
) : ViewModel() {

    val favoritesUiState: StateFlow<FavoritesUiState> =
        getFavoriteRecipesUseCase()
            .map { favoriteRecipes ->
                FavoritesUiState(
                    recipes = favoriteRecipes.map { it.toRecipeCardUiModel() }.toImmutableList()
                )
            }
            .catch {
                emit(FavoritesUiState(isError = true))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT),
                initialValue = FavoritesUiState()
            )
}
