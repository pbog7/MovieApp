package com.example.moviereviewsscreen

import androidx.lifecycle.viewModelScope
import com.example.common.base.BaseViewModelNoInitialState
import com.example.common.domain.models.CustomResult
import com.example.common.domain.useCases.GetMovieReviewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieReviewsViewModel @Inject constructor(private val getMovieReviewsUseCase: GetMovieReviewsUseCase) :
    BaseViewModelNoInitialState() {
    private val _viewState = MutableStateFlow(MovieReviewsViewState())
    val viewState: StateFlow<MovieReviewsViewState> = _viewState

    fun getMovieReviews(movieId: String) {
        viewModelScope.launch {
            getMovieReviewsUseCase(GetMovieReviewsUseCase.Params(movieId)).collect { result ->
                when (result) {
                    is CustomResult.Success -> _viewState.update {
                        it.copy(
                            loading = false,
                            movieReviews = result.data
                        )
                    }
                    is CustomResult.Failure<*> -> _viewState.update {
                        it.copy(
                            loading = false,
                            movieReviewsError = result.error.toString()
                        )
                    }
                }
            }
        }
    }
}