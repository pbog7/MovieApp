package com.example.common.domain.useCases

import com.example.common.base.BaseUseCase
import com.example.common.domain.models.CustomResult
import com.example.common.domain.models.FeedItem
import com.example.common.domain.models.MovieReview
import com.example.common.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMovieReviewsUseCase @Inject constructor(private val moviesRepo: MoviesRepository) :
    BaseUseCase<CustomResult<List<MovieReview>>, GetMovieReviewsUseCase.Params> {

    override suspend fun invoke(params: Params):
            Flow<CustomResult<List<MovieReview>>> =
        moviesRepo.getMovieReviews(params.movieId)

    data class Params(val movieId: String)
}