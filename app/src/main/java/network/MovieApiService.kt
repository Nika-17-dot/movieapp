package com.example.movieapp.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {


    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>


    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Response<MovieResponse>
}