package com.example.common.domain.useCases

import com.example.common.base.BaseUseCase
import com.example.common.base.BaseUseCaseNoParams
import com.example.common.domain.models.CustomResult
import com.example.common.domain.models.FeedItem
import com.example.common.domain.models.FeedItem.Movie
import com.example.common.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMovieUseCase @Inject constructor(private val moviesRepo: MoviesRepository) :
    BaseUseCase<CustomResult<List<Movie>>, SearchMovieUseCase.Params> {

    override suspend fun invoke(params: Params): Flow<CustomResult<List<Movie>>> =
        moviesRepo.searchMovie(params.query)

    data class Params(val query: String)
}