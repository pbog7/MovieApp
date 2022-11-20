package com.example.common.data.repositories

import com.example.common.data.db.dao.MovieDao
import com.example.common.data.network.NetworkApiService
import com.example.common.data.toMovie
import com.example.common.data.toMovieEntity
import com.example.common.data.toMovieReview
import com.example.common.domain.models.CustomException
import com.example.common.domain.models.CustomException.EmptyMoviesList
import com.example.common.domain.models.CustomException.NoReviews
import com.example.common.domain.models.FeedItem.Movie
import com.example.common.domain.models.CustomResult
import com.example.common.domain.models.MovieReview
import com.example.common.domain.repositories.MoviesRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val networkApi: NetworkApiService,
    private val movieDao: MovieDao
) :
    MoviesRepository {
    override suspend fun getTop250Movies(): Flow<CustomResult<List<Movie>>> = flow {
        val response = networkApi.getTop250Movies()
        with(response) {
            if (isSuccessful) {
                body()?.run {
                    top250Data.map {
                        it.toMovie()
                    }.also {
                        if (it.isEmpty()) {
                            if (errorMessage.isNullOrEmpty()) {
                                emit(CustomResult.Failure(EmptyMoviesList("The movies list is empty !")))
                            } else {
                                emit(CustomResult.Failure(errorMessage))
                            }
                        } else {
                            emit(CustomResult.Success(it))
                        }
                    }
                }
            } else {
                emit(CustomResult.Failure(response.errorBody()))
            }
        }
    }.catch { e ->
        emit(CustomResult.Failure(e))
    }

    override suspend fun addMovieToFavorites(movie: Movie): Flow<CustomResult<Unit>> = flow {
        try {
            movieDao.insert(movie.toMovieEntity())
            emit(CustomResult.Success(Unit))
        } catch (e: Exception) {
            emit(CustomResult.Failure(e))
        }
    }

    override suspend fun removeMovieFromFavorites(movie: Movie): Flow<CustomResult<Unit>> = flow {
        try {
            movieDao.delete(movie.toMovieEntity())
            emit(CustomResult.Success(Unit))
        } catch (e: Exception) {
            emit(CustomResult.Failure(e))
        }
    }

    override suspend fun getFavoriteMovies(): Flow<CustomResult<List<Movie>>> = flow {
        try {
            movieDao.getAll()?.collect { movieEntityList ->
                if (movieEntityList.isEmpty()) {
                    emit(CustomResult.Failure(EmptyMoviesList()))
                } else {
                    emit(CustomResult.Success(movieEntityList.map { it.toMovie() }))
                }
            }
            emit(CustomResult.Failure(EmptyMoviesList()))
        } catch (e: Exception) {
            emit(CustomResult.Failure(e))
        }

    }

    override suspend fun searchMovie(expression: String): Flow<CustomResult<List<Movie>>> = flow {
        val response = networkApi.searchMovie(expression = expression)
        with(response) {
            if (isSuccessful) {
                body()?.run {
                    results.map {
                        it.toMovie()
                    }.also {
                        if (it.isEmpty()) {
                            if (errorMessage.isEmpty()) {
                                emit(CustomResult.Failure(EmptyMoviesList("The movies list is empty !")))
                            } else {
                                emit(CustomResult.Failure(errorMessage))
                            }
                        } else {
                            emit(CustomResult.Success(it))
                        }
                    }
                }
            } else {
                emit(CustomResult.Failure(response.errorBody()))
            }
        }
    }.catch { e ->
        emit(CustomResult.Failure(e))
    }

    override suspend fun getMovieReviews(movieId: String): Flow<CustomResult<List<MovieReview>>> =
        flow {
            val response = networkApi.getMovieReviews(id = movieId)
            with(response) {
                if (isSuccessful) {
                    body()?.run {
                        items.map {
                            it.toMovieReview()
                        }.also {
                            if (it.isEmpty()) {
                                if (errorMessage.isEmpty()) {
                                    emit(CustomResult.Failure(NoReviews("The movies list is empty !")))
                                } else {
                                    emit(CustomResult.Failure(errorMessage))
                                }
                            } else {
                                emit(CustomResult.Success(it))
                            }
                        }
                    }
                } else {
                    emit(CustomResult.Failure(response.errorBody()))
                }
            }
        }.catch { e ->
            emit(CustomResult.Failure(e))
        }

    override suspend fun getAllMovieIds(): Flow<CustomResult<List<String>>> = flow {
        try {
            movieDao.getAllMovieIds()?.collect {
                emit(CustomResult.Success(it))
            }
            emit(CustomResult.Failure(EmptyMoviesList()))
        } catch (e: Exception) {
            emit(CustomResult.Failure(e))
        }
    }

    override suspend fun deleteAllFavorites(): Flow<CustomResult<Unit>> = flow {
        try {
            movieDao.deleteAll()
            emit(CustomResult.Success(Unit))
        } catch (e:Exception){
            emit(CustomResult.Failure(e))
        }
    }
}