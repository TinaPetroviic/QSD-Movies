package com.example.qsdmovies.network

import com.example.qsdmovies.models.CreditsResponse
import com.example.qsdmovies.models.MovieResponse
import com.example.qsdmovies.models.TopRatedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {

    @GET("/3/movie/popular")
    suspend fun getMovieListPaging(
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedListPaged(
        @Query("page") page: Int
    ): Response<TopRatedResponse>

    @GET("3/tv/popular")
    suspend fun getTvShowsList(
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("3/search/multi")
    suspend fun search(
        @Query("page") page: Int,
        @Query("query") query: String
    ): Response<MovieResponse>

    @GET("3/movie/{movieId}/credits")
    suspend fun getCredits(
        @Path("movieId") movieId: Int
    ): Response<CreditsResponse>
}