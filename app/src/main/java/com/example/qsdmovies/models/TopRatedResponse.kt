package com.example.qsdmovies.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopRatedResponse(
    @SerializedName("results")
    val toprated: List<TopRated>

) : Parcelable {
    constructor() : this(mutableListOf())
}