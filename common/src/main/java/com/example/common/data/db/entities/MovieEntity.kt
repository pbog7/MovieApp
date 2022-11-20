package com.example.common.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.common.domain.models.FeedItemType

@Entity
data class MovieEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "imdb_rating") val imdbRating: Float,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "crew") val crew: String,
    @ColumnInfo(name = "fullTitle") val fullTitle: String,
    @ColumnInfo(name = "imdbRatingCount") val imdbRatingCount: Int,
    @ColumnInfo(name = "rank") val rank: Int,
)