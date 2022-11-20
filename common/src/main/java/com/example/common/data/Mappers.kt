package com.example.common.data


import android.app.appsearch.SearchResult
import com.example.common.data.db.entities.MovieEntity
import com.example.common.data.network.dto.ReviewDetailDTO
import com.example.common.data.network.dto.SearchResultDTO
import com.example.common.data.network.dto.Top250DTO
import com.example.common.domain.models.FeedItem.*
import com.example.common.domain.models.FeedItemType
import com.example.common.domain.models.FeedItemType.*
import com.example.common.domain.models.MovieReview

fun Top250DTO.toMovie() = Movie(
    id = id,
    rank = rank?.toInt(),
    title = title.orEmpty(),
    fullTitle = fullTitle,
    year = year?.toInt(),
    image = image.orEmpty(),
    crew = crew,
    imdbRating = imdbRating?.toFloat(),
    imdbRatingCount = imdbRatingCount?.toInt(),
)

fun Movie.toMovieEntity() = MovieEntity(
    id = id,
    title = title,
    year = year ?: 0,
    imdbRating = imdbRating ?: 0f,
    image = image,
    crew = crew.orEmpty(),
    fullTitle = fullTitle.orEmpty(),
    imdbRatingCount = imdbRatingCount ?: 0,
    rank = rank ?: 0
)

fun MovieEntity.toMovie() = Movie(
    id = id,
    title = title,
    year = year,
    imdbRating = imdbRating,
    favorite = null,
    image = image,
    crew = "",
    fullTitle = "",
    imdbRatingCount = 0,
    rank = 0,
)

fun SearchResultDTO.toMovie() = Movie(
    feedItemType = SEARCH,
    id = id,
    title = title,
    image = image,
    description = description
)

fun ReviewDetailDTO.toMovieReview() = MovieReview(
    username = username,
    date = date,
    rate = rate,
    helpful = helpful,
    title = title,
    content = content
)
