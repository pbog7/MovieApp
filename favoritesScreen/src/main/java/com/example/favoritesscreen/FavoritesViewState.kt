package com.example.favoritesscreen

import com.example.common.domain.models.FeedItem.Movie

data class FavoritesViewState(
    val favorites: List<Movie>? = null,
    val loading: Boolean = true,
    val favoritesErrorMessage:String? = null
)
