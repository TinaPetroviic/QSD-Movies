package com.example.qsdmovies.models

import com.google.gson.annotations.SerializedName

data class TopRated(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("poster_path") val poster: String?,
    @SerializedName("relase date") val releaseDate: String?,
    @SerializedName("vote_count") val vote: String,
    @SerializedName("overview") val overview: String,
) {
    var favorite = false
}