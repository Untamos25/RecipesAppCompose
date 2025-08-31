package com.example.composeapp.domain.recipes.usecase

import com.example.composeapp.domain.repository.AppRepository
import javax.inject.Inject

class SyncRecipesForCategoryUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(categoryId: Int) = appRepository.syncRecipesForCategory(categoryId)
}
