package com.example.common.domain.models

sealed class AppEvent {
 data class GoToMovieReviews(val movieId: String):AppEvent()
}
