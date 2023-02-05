package com.example.qsdmovies.models

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("crew") val crew: List<Crew>,
    @SerializedName("cast") val casts: List<Cast>
)

data class Crew(
    @SerializedName("id") val id: Int,
    @SerializedName("profile_path") val profilePath: String,
    @SerializedName("name") val name: String,
    @SerializedName("known_for_department") val knownForDepartment: String,
    @SerializedName("department") val department: String,
    @SerializedName("job") val job: String
)

data class Cast(
    @SerializedName("id") val id: Int,
    @SerializedName("profile_path") val profilePath: String,
    @SerializedName("name") val name: String,
    @SerializedName("character") val character: String,
    @SerializedName("known_for_department") val knownForDepartment: String

)
