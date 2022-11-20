package com.example.common.domain.useCases

import com.example.common.base.BaseUseCaseNoParams
import com.example.common.domain.models.FeedItem.Movie
import com.example.common.domain.models.CustomResult
import com.example.common.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTop250MoviesUseCase @Inject constructor(private val moviesRepo: MoviesRepository) :
    BaseUseCaseNoParams<CustomResult<List<Movie>>> {

    override suspend fun invoke(): Flow<CustomResult<List<Movie>>> =
        moviesRepo.getTop250Movies()

}