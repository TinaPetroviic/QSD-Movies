package com.example.qsdmovies.models

import com.google.gson.annotations.SerializedName

data class TvShows(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("poster_path") val poster: String?,
    @SerializedName("relase date") val releaseDate: String?,
    @SerializedName("overview") val overview: String,
)