package com.example.composeapp.domain.categories.usecase

import com.example.composeapp.domain.repository.AppRepository
import javax.inject.Inject

class GetCategoryWithRecipesUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    operator fun invoke(categoryId: Int) = appRepository.getCategoryWithRecipes(categoryId)
}
