package com.example.searchscreen

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.common.base.BaseViewModel
import com.example.common.base.BaseViewModelNoInitialState
import com.example.common.domain.models.CustomException
import com.example.common.domain.models.CustomResult
import com.example.common.domain.models.FeedItem
import com.example.common.domain.models.FeedItem.Movie
import com.example.common.domain.useCases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMovieUseCase: SearchMovieUseCase,
    private val addMovieToFavoritesUseCase: AddMovieToFavoritesUseCase,
    private val removeMovieFromFavoritesUseCase: RemoveMovieFromFavoritesUseCase
) :
    BaseViewModelNoInitialState() {
    private val _viewState = MutableStateFlow(SearchViewState())
    val viewState: StateFlow<SearchViewState> = _viewState
    fun searchMovie(query: String) {
        _viewState.update { it.copy(loading = true) }
        viewModelScope.launch {
            searchMovieUseCase(SearchMovieUseCase.Params(query)).collect { result ->
                when (result) {
                    is CustomResult.Success -> _viewState.update {
                        it.copy(
                            searchResults = result.data,
                            loading = false
                        )
                    }
                    is CustomResult.Failure<*> -> if (result.error is CustomException.EmptyMoviesList) {
                        _viewState.update {
                            it.copy(
                                searchResults = emptyList(),
                                loading = false
                            )
                        }
                        Log.d("ErrorLog", "The are no movies !")
                    } else {
                        _viewState.update {
                            it.copy(
                                loading = false
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
                    emit(CustomResult.Success(Unit))
                }
                is CustomResult.Failure<*> -> {
                    emit(CustomResult.Failure(Unit))
                }
            }
        }
    }

    suspend fun removeFromFavorites(movie: Movie): Flow<CustomResult<Unit>> = flow {
        removeMovieFromFavoritesUseCase(RemoveMovieFromFavoritesUseCase.Params(movie)).collect { result ->
            when (result) {
                is CustomResult.Success -> {
                    emit(CustomResult.Success(Unit))
                }
                is CustomResult.Failure<*> -> {
                    emit(CustomResult.Failure(Unit))
                }
            }
        }
    }
}