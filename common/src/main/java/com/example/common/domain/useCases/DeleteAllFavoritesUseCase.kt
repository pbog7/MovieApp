package com.example.common.domain.useCases

import com.example.common.base.BaseUseCaseNoParams
import com.example.common.domain.models.CustomResult
import com.example.common.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteAllFavoritesUseCase @Inject constructor(private val moviesRepo: MoviesRepository) :
    BaseUseCaseNoParams<CustomResult<Unit>> {

    override suspend fun invoke(): Flow<CustomResult<Unit>> =
        moviesRepo.deleteAllFavorites()
}