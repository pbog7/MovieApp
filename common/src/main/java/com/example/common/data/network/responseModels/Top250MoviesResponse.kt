package com.example.common.data.network.responseModels

import com.example.common.data.network.dto.Top250DTO
import com.google.gson.annotations.SerializedName

data class Top250MoviesResponse(
    @SerializedName("items")
    val top250Data: List<Top250DTO>,
    @SerializedName("ErrorMessage")
    val errorMessage: String?
)
