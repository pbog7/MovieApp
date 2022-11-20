package com.example.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class Top250DTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("rank")
    val rank: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("fullTitle")
    val fullTitle: String?,
    @SerializedName("year")
    val year: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("crew")
    val crew: String?,
    @SerializedName("imDbRating")
    val imdbRating: String?,
    @SerializedName("imDbRatingCount")
    val imdbRatingCount: String?,
)