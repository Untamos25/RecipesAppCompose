package com.example.composeapp.domain.recipes.usecase

import com.example.composeapp.domain.common.repository.RecipesRepository
import com.example.composeapp.domain.recipes.model.Recipe

class GetRecipesByCategoryId(private val recipesRepository: RecipesRepository) {
    operator fun invoke(categoryId: Int): List<Recipe> {
        return recipesRepository.getRecipesByCategoryId(categoryId)
    }
}