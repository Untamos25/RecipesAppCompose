package com.pavlushinsa.recipescompapp.domain.categories.usecase

import com.pavlushinsa.recipescompapp.domain.repository.AppRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    operator fun invoke() = appRepository.getCategories()
}
