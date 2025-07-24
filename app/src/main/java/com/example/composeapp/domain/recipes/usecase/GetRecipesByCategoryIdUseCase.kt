package com.example.composeapp.domain.recipes.usecase

import com.example.composeapp.domain.repository.AppRepository
import com.example.composeapp.domain.recipes.model.Recipe
import javax.inject.Inject

class GetRecipesByCategoryIdUseCase @Inject constructor(private val recipesRepository: AppRepository) {
    suspend operator fun invoke(categoryId: Int): List<Recipe> {
        return recipesRepository.getRecipesByCategoryId(categoryId)
    }
}
