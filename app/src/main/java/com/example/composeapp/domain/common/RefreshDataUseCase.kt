package com.example.composeapp.domain.common

import com.example.composeapp.domain.repository.AppRepository
import javax.inject.Inject

class RefreshDataUseCase @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke() = appRepository.refreshData()
}
