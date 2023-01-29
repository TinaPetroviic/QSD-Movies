package com.example.qsdmovies.network

import com.example.qsdmovies.models.MovieResponse
import com.example.qsdmovies.models.TopRatedResponse
import com.example.qsdmovies.models.TvShowsResponse
import retrofit2.Call
import retrofit2.http.GET

interface MoviesAPI {
    @GET("/3/movie/popular")
    fun getMovieList(): Call<MovieResponse>

    @GET("/3/movie/top_rated")
    fun getTopRatedList(): Call<TopRatedResponse>

    @GET("3/tv/popular")
    fun getTvShowsList(): Call<TvShowsResponse>
}