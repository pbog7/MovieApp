package com.example.searchscreen

import android.content.SearchRecentSuggestionsProvider

class MoviesSearchSuggestionsProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.example.searchscreen.MoviesSearchSuggestionsProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}