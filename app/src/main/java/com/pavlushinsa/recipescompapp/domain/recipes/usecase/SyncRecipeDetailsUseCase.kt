package com.pavlushinsa.recipescompapp.domain.recipes.usecase

import com.pavlushinsa.recipescompapp.domain.common.DataResult
import com.pavlushinsa.recipescompapp.domain.common.Error
import com.pavlushinsa.recipescompapp.domain.repository.AppRepository
import javax.inject.Inject

class SyncRecipeDetailsUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(recipeId: Int): DataResult<Unit, Error> =
        appRepository.syncRecipeDetails(recipeId)
}
