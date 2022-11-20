package com.example.common.domain.models

data class MovieReview(
    val username: String,
    val date: String,
    val rate: String,
    val helpful: String,
    val title: String,
    val content: String,
) {
    override fun equals(other: Any?): Boolean = when {
        this === other -> true
        javaClass != other?.javaClass -> false
        else -> {
            other as MovieReview
            (username == other.username && title == other.title && date == other.date &&
                    rate == other.rate && helpful == other.helpful && content == other.content)
        }
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + rate.hashCode()
        result = 31 * result + helpful.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + content.hashCode()
        return result
    }
}
