package com.example.composeapp.domain.categories.usecase

import com.example.composeapp.domain.repository.AppRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    operator fun invoke() = appRepository.getCategories()
}
