package com.example.qsdmovies.services

import com.example.qsdmovies.models.TvShowsResponse
import retrofit2.Call
import retrofit2.http.GET

interface TvShowsApiInterface {

    @GET("/3/tv/popular?api_key=330ead338b8ebe27064c777e1b7ee86d")
    fun getTvShowsList(): Call<TvShowsResponse>

}