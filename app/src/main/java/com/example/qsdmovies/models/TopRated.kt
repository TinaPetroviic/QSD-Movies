package com.example.qsdmovies.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopRated(
    @SerializedName("id")
    val id: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("poster_path")
    val poster: String?,

    @SerializedName("relase date")
    val relase: String?,

    @SerializedName("vote_count")
    val vote: String


) : Parcelable {
    constructor() : this("", "", "", "", "")
}