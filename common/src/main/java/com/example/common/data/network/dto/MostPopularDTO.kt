package com.example.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class MostPopularDTO(
    @SerializedName("Id")
    val id: String,
    @SerializedName("Rank")
    val rank: String,
    @SerializedName("RankUpDown")
    val rankUpDown: String,
    @SerializedName("Title")
    val title: String,
    @SerializedName("FullTitle")
    val fullTitle: String,
    @SerializedName("Year")
    val year: String,
    @SerializedName("Image")
    val image: String,
    @SerializedName("Crew")
    val crew: String,
    @SerializedName("IMDbRating")
    val imdbRating: String,
    @SerializedName("IMDbRatingCount")
    val imdbRatingCount: String
)