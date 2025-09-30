package com.pavlushinsa.recipescompapp.presentation.common.mapper

import androidx.annotation.StringRes
import com.pavlushinsa.recipescompapp.R
import com.pavlushinsa.recipescompapp.domain.common.Error
import com.pavlushinsa.recipescompapp.presentation.common.model.UiErrorType

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
