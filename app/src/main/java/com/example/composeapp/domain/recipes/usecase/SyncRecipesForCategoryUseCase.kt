package com.example.composeapp.domain.recipes.usecase

import com.example.composeapp.domain.common.DataResult
import com.example.composeapp.domain.common.Error
import com.example.composeapp.domain.repository.AppRepository
import javax.inject.Inject

class SyncRecipesForCategoryUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(categoryId: Int): DataResult<Unit, Error> =
        appRepository.syncRecipesForCategory(categoryId)
}
