package com.example.common.data.network

import com.example.common.data.network.responseModels.MovieReviewsResponse
import com.example.common.data.network.responseModels.SearchResponse
import com.example.common.data.network.responseModels.Top250MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApiService {
    @GET("{$LANGUAGE}/API/Top250Movies/{$API_KEY}")
    suspend fun getTop250Movies(
        @Path(LANGUAGE) language: String = ENGLISH,
        @Path(API_KEY) apiKey: String = API_KEY_VALUE
    ): Response<Top250MoviesResponse>

    @GET("{$LANGUAGE}/API/SearchMovie/{$API_KEY}/{$EXPRESSION}")
    suspend fun searchMovie(
        @Path(LANGUAGE) language: String = ENGLISH,
        @Path(API_KEY) apiKey: String = API_KEY_VALUE,
        @Path(EXPRESSION) expression: String
    ): Response<SearchResponse>

    @GET("{$LANGUAGE}/API/Reviews/{$API_KEY}/{$ID}")
    suspend fun getMovieReviews(
        @Path(LANGUAGE) language: String = ENGLISH,
        @Path(API_KEY) apiKey: String = API_KEY_VALUE,
        @Path(ID) id: String
    ): Response<MovieReviewsResponse>
}