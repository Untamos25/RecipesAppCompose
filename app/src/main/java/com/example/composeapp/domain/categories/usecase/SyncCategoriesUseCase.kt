package com.example.composeapp.domain.categories.usecase

import com.example.composeapp.domain.common.DataResult
import com.example.composeapp.domain.common.Error
import com.example.composeapp.domain.repository.AppRepository
import javax.inject.Inject

class SyncCategoriesUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(): DataResult<Unit, Error> = appRepository.syncCategories()
}
