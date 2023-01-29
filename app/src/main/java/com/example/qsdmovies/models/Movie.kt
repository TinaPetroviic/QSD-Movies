package com.example.qsdmovies.models

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("poster_path") val poster: String?,
    @SerializedName("relase date") val releaseDate: String?,
    @SerializedName("rating") val rating: String,
    @SerializedName("video") val video: Boolean
)