package com.example.qsdmovies.models

import com.google.gson.annotations.SerializedName

data class TvShowsResponse(
    @SerializedName("results")
    val tvShows: List<TvShows>
)
