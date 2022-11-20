package com.example.common.domain.useCases

import com.example.common.base.BaseUseCase
import com.example.common.domain.models.CustomResult
import com.example.common.domain.models.FeedItem
import com.example.common.domain.models.FeedItem.Movie
import com.example.common.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddMovieToFavoritesUseCase @Inject constructor(private val moviesRepo: MoviesRepository) :
    BaseUseCase<CustomResult<Unit>, AddMovieToFavoritesUseCase.Params> {

    override suspend fun invoke(params: Params): Flow<CustomResult<Unit>> =
        moviesRepo.addMovieToFavorites(params.movie)

    data class Params(val movie: Movie)
}