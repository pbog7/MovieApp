package com.example.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class ReviewDetailDTO(
    @SerializedName("username")
    val username: String,
    @SerializedName("userUrl")
    val userUrl: String,
    @SerializedName("reviewLink")
    val reviewLink: String,
    @SerializedName("warningSpoilers")
    val warningSpoilers: Boolean,
    @SerializedName("date")
    val date: String,
    @SerializedName("rate")
    val rate: String,
    @SerializedName("helpful")
    val helpful: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
)

