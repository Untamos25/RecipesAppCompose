package com.example.composeapp.domain.categories.usecase

import com.example.composeapp.domain.categories.model.Category
import com.example.composeapp.domain.repository.AppRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val appRepository: AppRepository) {
    suspend operator fun invoke(): List<Category> {
        return appRepository.getCategories()
    }
}
