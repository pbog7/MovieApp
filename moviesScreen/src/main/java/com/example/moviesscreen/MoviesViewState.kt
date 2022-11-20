package com.example.moviesscreen

import com.example.common.domain.models.FeedItem.Movie

data class MoviesViewState(
    val moviesList: List<Movie>? = null,
    val loading: Boolean = true,
    val movieErrorMessage:String? = null,
    val addToFavoritesErrorMessage:String? = null
)
