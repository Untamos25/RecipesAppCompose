package com.example.composeapp.domain.recipes.usecase

import com.example.composeapp.domain.repository.AppRepository
import javax.inject.Inject

class GetFavoriteRecipesUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    operator fun invoke() = appRepository.getFavoriteRecipes()
}
