package com.example.qsdmovies.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TvShowsResponse(
    @SerializedName("results")
    val tvshows: List<TvShows>

) : Parcelable {
    constructor() : this(mutableListOf())
}