package com.pavlushinsa.recipescompapp.domain.recipes.usecase

import com.pavlushinsa.recipescompapp.domain.common.DataResult
import com.pavlushinsa.recipescompapp.domain.common.Error
import com.pavlushinsa.recipescompapp.domain.repository.AppRepository
import javax.inject.Inject

class SyncRecipesForCategoryUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(categoryId: Int): DataResult<Unit, Error> =
        appRepository.syncRecipesForCategory(categoryId)
}
