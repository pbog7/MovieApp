package com.example.common.data.network.dto

import com.google.gson.annotations.SerializedName

data class SearchResultDTO(
    @SerializedName("id")
    val id: String,
    @SerializedName("resultType")
    val resultType: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String
)