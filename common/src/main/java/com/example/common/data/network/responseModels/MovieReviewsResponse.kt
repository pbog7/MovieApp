package com.example.common.data.network.responseModels

import com.example.common.data.network.dto.ReviewDetailDTO
import com.google.gson.annotations.SerializedName

data class MovieReviewsResponse(
    @SerializedName("imDbId")
    val imdbId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("fullTitle")
    val fullTitle: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("year")
    val year: String,
    @SerializedName("items")
    val items:List<ReviewDetailDTO>,
    @SerializedName("errorMessage")
    val errorMessage: String
)
