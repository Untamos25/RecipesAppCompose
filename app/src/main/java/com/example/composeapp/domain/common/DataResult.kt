package com.example.composeapp.domain.common

sealed class DataResult<out T, out E> {
    data class Success<out T>(val value: T) : DataResult<T, Nothing>()
    data class Failure<out E>(val error: E) : DataResult<Nothing, E>()
}
