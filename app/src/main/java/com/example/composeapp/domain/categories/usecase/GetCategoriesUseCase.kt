package com.example.composeapp.domain.categories.usecase

import com.example.composeapp.domain.categories.model.Category
import com.example.composeapp.domain.common.repository.RecipesRepository

class GetCategoriesUseCase(private val recipesRepository: RecipesRepository) {
    operator fun invoke(): List<Category> {
        return recipesRepository.getCategories()
    }
}