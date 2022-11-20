package com.example.common.domain.useCases

import com.example.common.base.BaseUseCase
import com.example.common.domain.models.CustomResult
import com.example.common.domain.models.FeedItem
import com.example.common.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveMovieFromFavoritesUseCase @Inject constructor(private val moviesRepo: MoviesRepository) :
    BaseUseCase<CustomResult<Unit>, RemoveMovieFromFavoritesUseCase.Params> {

    override suspend fun invoke(params: Params): Flow<CustomResult<Unit>> =
        moviesRepo.removeMovieFromFavorites(params.movie)

    data class Params(val movie: FeedItem.Movie)
}