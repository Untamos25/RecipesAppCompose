package com.pavlushinsa.recipescompapp.domain.categories.usecase

import com.pavlushinsa.recipescompapp.domain.repository.AppRepository
import javax.inject.Inject

class GetCategoryWithRecipesUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    operator fun invoke(categoryId: Int) = appRepository.getCategoryWithRecipes(categoryId)
}
