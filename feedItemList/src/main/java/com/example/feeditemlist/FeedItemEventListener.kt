package com.example.feeditemlist

import com.example.common.domain.models.FeedItem
import com.example.common.domain.models.FeedItem.Movie

interface FeedItemEventListener {
    fun onItemClick(feedItem: Movie)
    fun onFavoriteClick(feedItem: Movie, position: Int)
}