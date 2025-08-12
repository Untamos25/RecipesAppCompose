package com.example.composeapp.domain.recipes.usecase

import com.example.composeapp.domain.repository.AppRepository
import javax.inject.Inject

class UpdateFavoriteStatusUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(recipeId: Int, isFavorite: Boolean) {
        appRepository.updateFavoriteStatus(recipeId, isFavorite)
    }
}
