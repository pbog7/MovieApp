package com.example.searchscreen

import com.example.common.domain.models.FeedItem.Movie

data class SearchViewState(
    val searchResults: List<Movie>? = null,
    val loading: Boolean = false,
    val searchError:String? = null
)
