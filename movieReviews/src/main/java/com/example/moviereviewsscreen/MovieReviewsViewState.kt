package com.example.moviereviewsscreen

import com.example.common.domain.models.MovieReview

data class MovieReviewsViewState(
    val loading: Boolean = true,
    val movieReviews: List<MovieReview>? = null,
    val movieReviewsError: String? = null
)