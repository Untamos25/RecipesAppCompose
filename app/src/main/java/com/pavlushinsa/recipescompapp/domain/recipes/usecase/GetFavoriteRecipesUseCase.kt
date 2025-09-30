package com.pavlushinsa.recipescompapp.domain.recipes.usecase

import com.pavlushinsa.recipescompapp.domain.repository.AppRepository
import javax.inject.Inject

class GetFavoriteRecipesUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    operator fun invoke() = appRepository.getFavoriteRecipes()
}
