package com.example.composeapp.domain.recipes.usecase

import com.example.composeapp.domain.common.repository.RecipesRepository
import com.example.composeapp.domain.recipes.model.Recipe

class GetRecipeById(private val recipesRepository: RecipesRepository) {
    operator fun invoke(recipeId: Int): Recipe? {
        return recipesRepository.getRecipeById(recipeId)
    }
}