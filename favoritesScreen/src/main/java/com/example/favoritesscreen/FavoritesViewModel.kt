package com.example.favoritesscreen

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.common.base.BaseViewModel
import com.example.common.domain.models.CustomException
import com.example.common.domain.models.CustomResult
import com.example.common.domain.models.FeedItem
import com.example.common.domain.models.FeedItem.Movie
import com.example.common.domain.useCases.AddMovieToFavoritesUseCase
import com.example.common.domain.useCases.GetFavoriteMoviesUseCase
import com.example.common.domain.useCases.RemoveMovieFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val addMovieToFavoritesUseCase: AddMovieToFavoritesUseCase,
    private val removeMovieFromFavoritesUseCase: RemoveMovieFromFavoritesUseCase
) :
    BaseViewModel() {
    private val _viewState = MutableStateFlow(FavoritesViewState())
    val viewState: StateFlow<FavoritesViewState> = _viewState
    private suspend fun getFavoriteMovies() {
        getFavoriteMoviesUseCase().collect { result ->
            when (result) {
                is CustomResult.Success -> _viewState.update {
                    it.copy(
                        favorites = result.data,
                        loading = false,
                        favoritesErrorMessage = null
                    )
                }
                is CustomResult.Failure<*> -> {
                    if (result.error is CustomException.EmptyMoviesList) {
                        _viewState.update {
                            it.copy(
                                favorites = emptyList(),
                                loading = false,
                                favoritesErrorMessage = (result.error as CustomException.EmptyMoviesList).message
                            )
                        }
                        Log.d("ErrorLog", "The are no movies !")
                    } else {
                        _viewState.update {
                            it.copy(
                                favorites = emptyList(),
                                loading = false,
                                favoritesErrorMessage = (result.error.toString())
                            )
                        }
                        Log.d("ErrorLog", "${result.error}")
                    }
                }
            }
        }
    }

    suspend fun addToFavorites(movie: Movie): Flow<CustomResult<Unit>> = flow {
        addMovieToFavoritesUseCase(AddMovieToFavoritesUseCase.Params(movie)).collect { result ->
            when (result) {
                is CustomResult.Success -> {
                    _viewState.update {
                        it.copy(favoritesErrorMessage = null)
                    }
                    emit(CustomResult.Success(Unit))
                }
                is CustomResult.Failure<*> -> {
                    _viewState.update {
                        it.copy(favoritesErrorMessage = result.error.toString())
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
                        it.copy(favoritesErrorMessage = null)
                    }
                    emit(CustomResult.Success(Unit))
                }
                is CustomResult.Failure<*> -> {
                    _viewState.update {
                        it.copy(favoritesErrorMessage = result.error.toString())
                    }
                    emit(CustomResult.Failure(Unit))
                }
            }
        }
    }

    override fun loadState() {
        _viewState.update { it.copy(loading = true) }
        viewModelScope.launch {
            getFavoriteMovies()
        }
    }

    init {
        loadState()
    }
}