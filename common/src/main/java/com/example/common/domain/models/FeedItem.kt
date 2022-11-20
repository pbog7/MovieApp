package com.example.common.domain.models

import com.example.common.domain.models.FeedItemType.*

sealed class FeedItem(
    open val feedItemType: FeedItemType,
    open val id: String,
    open val image: String,
    open val title: String
) {
    data class Movie(
        override val feedItemType: FeedItemType = MOVIE,
        override val id: String,
        val rank: Int? = null,
        val rankUpDown: String? = null,
        override val title: String,
        val fullTitle: String? = null,
        val year: Int? = null,
        override val image: String,
        val crew: String? = null,
        val imdbRating: Float? = null,
        val imdbRatingCount: Int? = null,
        var favorite: Boolean? = false,
        val description:String? = null
    ) : FeedItem(feedItemType = feedItemType, id = id, image = image, title = title) {
        override fun equals(other: Any?) = when {
            this === other -> true
            javaClass != other?.javaClass -> false
            else -> {
                other as Movie
                (id == other.id && title == other.title && rank == other.rank && rankUpDown == other.rankUpDown && fullTitle == other.fullTitle
                        && year == other.year && image == other.image
                        && crew == other.crew && imdbRating == other.imdbRating
                        && imdbRatingCount == other.imdbRatingCount)
            }
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + rank.hashCode()
            result = 31 * result + (rankUpDown?.hashCode() ?: 0)
            result = 31 * result + title.hashCode()
            result = 31 * result + fullTitle.hashCode()
            result = 31 * result + year.hashCode()
            result = 31 * result + image.hashCode()
            result = 31 * result + crew.hashCode()
            result = 31 * result + imdbRating.hashCode()
            result = 31 * result + imdbRatingCount.hashCode()
            return result
        }

        fun deepCopy() = Movie(
            id = id,
            rank = rank,
            title = title,
            fullTitle = fullTitle,
            year = year,
            image = image,
            crew = crew,
            imdbRating = imdbRating,
            imdbRatingCount = imdbRatingCount,
            favorite = favorite
        )
    }

    data class Ad(override val id: String, override val image: String, override val title: String) :
        FeedItem(
            feedItemType = AD,
            id = id,
            image = image,
            title = title
        ) {
        override fun equals(other: Any?): Boolean = when {
            this === other -> true
            javaClass != other?.javaClass -> false
            else -> {
                other as Ad
                (id == other.id && title == other.title && image == other.image)
            }
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + image.hashCode()
            result = 31 * result + title.hashCode()
            return result
        }
    }
}