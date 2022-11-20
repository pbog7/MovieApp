package com.example.common.base

import kotlinx.coroutines.flow.Flow

interface BaseUseCase<out T, in Params> {

    suspend operator fun invoke(params: Params): Flow<T>
}