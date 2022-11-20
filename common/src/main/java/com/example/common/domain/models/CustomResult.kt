package com.example.common.domain.models

sealed class CustomResult<out T> {
    data class Success<out T>(val data: T) : CustomResult<T>()
    data class Failure<out E>(val error: E) : CustomResult<Nothing>()
}