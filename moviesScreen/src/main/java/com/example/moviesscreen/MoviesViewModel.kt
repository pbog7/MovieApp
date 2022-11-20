package com.example.moviesscreen

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.common.base.BaseViewModel
import com.example.common.domain.models.CustomException.EmptyMoviesList
import com.example.common.domain.models.FeedItem.Movie
import com.example.common.domain.models.CustomResult
import com.example.common.domain.useCases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getTop250MoviesUseCase: GetTop250MoviesUseCase,
    private val getFavoriteMovieIdsUseCase: GetFavoriteMovieIdsUseCase,
    private val addMovieToFavoritesUseCase: AddMovieToFavoritesUseCase,
    private val removeMovieFromFavoritesUseCase: RemoveMovieFromFavoritesUseCase
) : BaseViewModel() {
    private val _viewState = MutableStateFlow(MoviesViewState())
    val viewState: StateFlow<MoviesViewState> = _viewState
    private suspend fun getMovies() {
        getTop250MoviesUseCase().combine(getFavoriteMovieIdsUseCase()) { topMovies, favoriteMovieIds ->
            when (topMovies) {
                is CustomResult.Success -> {
                    when (favoriteMovieIds) {
                        is CustomResult.Success -> {
                            for (movieItem in topMovies.data) {
                                movieItem.favorite =
                                    favoriteMovieIds.data.contains(movieItem.id)
                            }
                            _viewState.update {
                                it.copy(movieErrorMessage = null)
                            }
                        }
                        is CustomResult.Failure<*> -> {
                            _viewState.update {
                                it.copy(
                                    movieErrorMessage = "Favorite movies Error !"
                                )
                            }
                            Log.d("ErrorLog", "Favorite movies Error ${favoriteMovieIds.error}")
                        }
                    }
                    _viewState.update {
                        it.copy(
                            moviesList = topMovies.data,
                            loading = false,
                        )
                    }
                }
                is CustomResult.Failure<*> -> {
                    if (topMovies.error is EmptyMoviesList) {
                        _viewState.update {
                            it.copy(
                                moviesList = emptyList(),
                                loading = false,
                                movieErrorMessage = (topMovies.error as EmptyMoviesList).message
                            )
                        }
                        Log.d("ErrorLog", "The are no movies !")
                    } else {
                        _viewState.update {
                            it.copy(
                                moviesList = emptyList(),
                                loading = false,
                                movieErrorMessage = (topMovies.error.toString())
                            )
                        }
                        Log.d("ErrorLog", "${topMovies.error}")
                    }
                }
            }
        }.collect()
    }

    suspend fun addToFavorites(movie: Movie): Flow<CustomResult<Unit>> = flow {
        addMovieToFavoritesUseCase(AddMovieToFavoritesUseCase.Params(movie)).collect { result ->
            when (result) {
                is CustomResult.Success -> {
                    _viewState.update {
                        it.copy(addToFavoritesErrorMessage = null)
                    }
                    emit(CustomResult.Success(Unit))
                }
                is CustomResult.Failure<*> -> {
                    _viewState.update {
                        it.copy(addToFavoritesErrorMessage = result.error.toString())
                    }
                    emit(CustomResult.Failure(Unit))
                }
            }
        }
    }

    suspend fun removeFromFavorites(movie: Movie): Flow<CustomResult<Unit>> = flow {
        removeMovieFromFavoritesUseCase(RemoveMovieFromFavoritesUseCase.Params(movie)).collect { result ->
            when (result) {
                is CustomResult.Success -> {
                    _viewState.update {
                        it.copy(addToFavoritesErrorMessage = null)
                    }
                    emit(CustomResult.Success(Unit))
                }
                is CustomResult.Failure<*> -> {
                    _viewState.update {
                        it.copy(addToFavoritesErrorMessage = result.error.toString())
                    }
                    emit(CustomResult.Failure(Unit))
                }
            }
        }
    }

    override fun loadState() {
        _viewState.update { it.copy(loading = true) }
        viewModelScope.launch {
            getMovies()
        }
    }

    init {
        loadState()
    }

}