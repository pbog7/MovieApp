package com.example.common.domain.models

import java.lang.Exception

sealed class CustomException : Exception() {
    data class EmptyMoviesList(override val message: String = "The are no movies !") :
        CustomException()

    data class NoReviews(override val message: String = "There are no reviews for this movie yet") :
        CustomException()
}
