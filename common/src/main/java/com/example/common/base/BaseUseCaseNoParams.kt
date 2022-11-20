package com.example.common.base

import kotlinx.coroutines.flow.Flow

interface BaseUseCaseNoParams<out T> {

    suspend operator fun invoke(): Flow<T>
}