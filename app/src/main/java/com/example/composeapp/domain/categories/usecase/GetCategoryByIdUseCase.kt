package com.example.composeapp.domain.categories.usecase

import com.example.composeapp.domain.categories.model.Category
import com.example.composeapp.domain.repository.AppRepository
import javax.inject.Inject

class GetCategoryByIdUseCase @Inject constructor(
    private val recipesRepository: AppRepository
) {
    suspend operator fun invoke(categoryId: Int): Category? {
        return recipesRepository.getCategoryById(categoryId)
    }
}
