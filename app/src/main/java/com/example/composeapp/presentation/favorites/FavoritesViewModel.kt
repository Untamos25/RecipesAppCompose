package com.example.composeapp.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.domain.recipes.usecase.GetFavoriteRecipesUseCase
import com.example.composeapp.presentation.favorites.model.FavoritesUiState
import com.example.composeapp.presentation.recipes.list.mapper.toRecipeCardUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase
) : ViewModel() {

    private val _favoritesUiState = MutableStateFlow(FavoritesUiState())
    val favoritesUiState = _favoritesUiState.asStateFlow()

    init {
        observeFavoriteRecipes()
    }

    private fun observeFavoriteRecipes() {
        viewModelScope.launch {
            getFavoriteRecipesUseCase()
                .catch {
                    _favoritesUiState.update { it.copy(isError = true) }
                    it.printStackTrace()
                }
                .collect { favoriteRecipes ->
                    _favoritesUiState.update {
                        it.copy(
                            recipes = favoriteRecipes.map { recipe ->
                                recipe.toRecipeCardUiModel()
                            }.toImmutableList()
                        )
                    }
                }
        }
    }
}
