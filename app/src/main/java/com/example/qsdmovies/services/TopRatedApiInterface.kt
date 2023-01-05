package com.example.qsdmovies.services


import com.example.qsdmovies.models.TopRatedResponse
import retrofit2.Call
import retrofit2.http.GET

interface TopRatedApiInterface {

    @GET("/3/movie/top_rated?api_key=330ead338b8ebe27064c777e1b7ee86d")
    fun getTopRatedList(): Call<TopRatedResponse>
}