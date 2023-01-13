package com.example.qsdmovies.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    @SerializedName("id")
    val id: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("poster_path")
    val poster: String?,

    @SerializedName("relase date")
    val relase: String?,

    @SerializedName("rating")
    val rating: String,

    @SerializedName("video")
    val video: Boolean



) : Parcelable {
    constructor() : this("", "", "", "", "",true)
}