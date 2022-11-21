package com.example.common.data.network.responseModels

import android.app.appsearch.SearchResult
import com.example.common.data.network.dto.SearchResultDTO
import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("searchType")
    val searchType: String,
    @SerializedName("expression")
    val expression: String,
    @SerializedName("results")
    val results: List<SearchResultDTO>?,
    @SerializedName("errorMessage")
    val errorMessage: String
)