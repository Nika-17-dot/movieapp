package com.example.movieapp.network

import com.google.gson.annotations.SerializedName


data class MovieResponse(
    val results: List<Movie>
)


data class Movie(
    val id: Int,
    val title: String,

    @SerializedName("poster_path")
    val poster_path: String?,

    @SerializedName("overview")
    val overview: String
)