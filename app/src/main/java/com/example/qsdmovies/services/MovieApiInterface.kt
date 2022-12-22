package com.example.qsdmovies.services

import com.example.qsdmovies.models.MovieResponse
import retrofit2.Call
import retrofit2.http.GET

interface MovieApiInterface {

    @GET("/movie/popular?api_key=330ead338b8ebe27064c777e1b7ee86d")
    fun getMovieList(): Call<MovieResponse>
}