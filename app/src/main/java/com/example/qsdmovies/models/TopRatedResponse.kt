package com.example.qsdmovies.models

import com.google.gson.annotations.SerializedName

data class TopRatedResponse(
    @SerializedName("results")
    val topRated: List<TopRated>
)
