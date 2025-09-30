package com.pavlushinsa.recipescompapp.presentation.common.model

enum class UiErrorType(val isRetryable: Boolean) {
    NoInternet(isRetryable = true),
    Server(isRetryable = true),
    Database(isRetryable = false),
    NotFound(isRetryable = false),
    AccessDenied(isRetryable = false),
    Unknown(isRetryable = false)
}
