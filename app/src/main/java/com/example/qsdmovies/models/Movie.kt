package com.example.qsdmovies.models

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("poster_path") val poster_path: String,
    @SerializedName("vote_average") val vote_average: Float,
    @SerializedName("media_type") val media_type: String? = null
) {

    var favorite = false
}