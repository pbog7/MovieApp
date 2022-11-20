package com.example.common.domain.repositories

import com.example.common.domain.models.FeedItem.Movie
import com.example.common.domain.models.CustomResult
import com.example.common.domain.models.MovieReview
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun getTop250Movies(): Flow<CustomResult<List<Movie>>>
    suspend fun addMovieToFavorites(movie: Movie): Flow<CustomResult<Unit>>
    suspend fun removeMovieFromFavorites(movie: Movie): Flow<CustomResult<Unit>>
    suspend fun getFavoriteMovies(): Flow<CustomResult<List<Movie>>>
    suspend fun searchMovie(expression: String): Flow<CustomResult<List<Movie>>>
    suspend fun getMovieReviews(movieId: String): Flow<CustomResult<List<MovieReview>>>
    suspend fun getAllMovieIds():Flow<CustomResult<List<String>>>
    suspend fun deleteAllFavorites():Flow<CustomResult<Unit>>
}