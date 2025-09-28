package com.example.composeapp.presentation.common.mapper

import androidx.annotation.StringRes
import com.example.composeapp.R
import com.example.composeapp.domain.common.Error
import com.example.composeapp.presentation.common.model.UiErrorType

fun Error.toUiErrorType(): UiErrorType {
    return when (this) {
        Error.NoInternetConnection -> UiErrorType.NoInternet
        Error.ServerError -> UiErrorType.Server
        Error.NotFound -> UiErrorType.NotFound
        Error.Unauthorized -> UiErrorType.AccessDenied
        Error.DatabaseError -> UiErrorType.Database
        Error.UnknownError -> UiErrorType.Unknown
    }
}

@StringRes
fun UiErrorType.toStringResId(): Int {
    return when (this) {
        UiErrorType.NoInternet -> R.string.error_no_internet
        UiErrorType.Server -> R.string.error_server
        UiErrorType.Database -> R.string.error_database
        UiErrorType.NotFound -> R.string.error_not_found
        UiErrorType.AccessDenied -> R.string.error_access_denied
        UiErrorType.Unknown -> R.string.error_unknown
    }
}
