package com.example.composeapp.domain.recipes.usecase

import com.example.composeapp.domain.repository.AppRepository
import javax.inject.Inject

class GetRecipeDetailsUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    operator fun invoke(recipeId: Int) = appRepository.getRecipeWithIngredients(recipeId)
}
