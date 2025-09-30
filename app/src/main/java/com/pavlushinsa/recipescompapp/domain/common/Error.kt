package com.pavlushinsa.recipescompapp.domain.common

sealed class Error {
    data object NoInternetConnection : Error()
    data object ServerError : Error()
    data object NotFound : Error()
    data object Unauthorized : Error()
    data object DatabaseError : Error()
    data object UnknownError : Error()
}
