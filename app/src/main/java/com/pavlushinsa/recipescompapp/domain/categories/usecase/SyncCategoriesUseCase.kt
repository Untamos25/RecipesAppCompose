package com.pavlushinsa.recipescompapp.domain.categories.usecase

import com.pavlushinsa.recipescompapp.domain.common.DataResult
import com.pavlushinsa.recipescompapp.domain.common.Error
import com.pavlushinsa.recipescompapp.domain.repository.AppRepository
import javax.inject.Inject

class SyncCategoriesUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(): DataResult<Unit, Error> = appRepository.syncCategories()
}
