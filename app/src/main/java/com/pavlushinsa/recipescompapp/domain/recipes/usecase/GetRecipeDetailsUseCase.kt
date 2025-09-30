package com.pavlushinsa.recipescompapp.domain.recipes.usecase

import com.pavlushinsa.recipescompapp.domain.repository.AppRepository
import javax.inject.Inject

class GetRecipeDetailsUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    operator fun invoke(recipeId: Int) = appRepository.getRecipeWithIngredients(recipeId)
}
