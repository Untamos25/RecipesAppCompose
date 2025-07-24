package com.example.composeapp.domain.recipes.usecase

import com.example.composeapp.domain.recipes.model.Recipe
import com.example.composeapp.domain.repository.AppRepository
import javax.inject.Inject

class GetRecipeByIdUseCase @Inject constructor(private val recipesRepository: AppRepository) {
    suspend operator fun invoke(recipeId: Int): Recipe? {
        return recipesRepository.getRecipeById(recipeId)
    }
}
