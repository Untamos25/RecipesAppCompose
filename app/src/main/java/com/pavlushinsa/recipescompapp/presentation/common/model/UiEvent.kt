package com.pavlushinsa.recipescompapp.presentation.common.model

sealed class UiEvent {
    data class ShowSnackBarEvent(
        val errorType: UiErrorType,
        val onRetry: (() -> Unit)? = null
    ) : UiEvent()
}
