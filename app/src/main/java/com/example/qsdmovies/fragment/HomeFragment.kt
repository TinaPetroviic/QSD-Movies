package com.example.qsdmovies.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.qsdmovies.R
import com.example.qsdmovies.adapter.MovieAdapter
import com.example.qsdmovies.adapter.TopRatedAdapter
import com.example.qsdmovies.adapter.TvShowsAdapter
import com.example.qsdmovies.models.*
import com.example.qsdmovies.services.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("HomeFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewWatching.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewWatching.setHasFixedSize(true)
        getMovieData { movies: List<Movie> ->
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + movies.first().poster)
                .into(poster)
            recyclerViewWatching.adapter = MovieAdapter(movies)
        }

        recyclerViewTopRated.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewTopRated.setHasFixedSize(true)
        getTopRatedList { toprated: List<TopRated> ->
            recyclerViewTopRated.adapter = TopRatedAdapter(toprated)
        }
        recyclerViewTVShows.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewTVShows.setHasFixedSize(true)
        getTvShowsList { tvshows: List<TvShows> ->
            recyclerViewTVShows.adapter = TvShowsAdapter(tvshows)
        }

        recyclerViewPopularMovies.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPopularMovies.setHasFixedSize(true)
        getMovieData { movies: List<Movie> ->
            recyclerViewPopularMovies.adapter = MovieAdapter(movies)
        }
    }

    private fun getMovieData(callback: (List<Movie>) -> Unit) {
        val apiService = MoviesApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable?) {

            }
        })
    }

    private fun getTopRatedList(callback: (List<TopRated>) -> Unit) {
        val apiService = TopRatedApiService.getInstance().create(TopRatedApiInterface::class.java)
        apiService.getTopRatedList().enqueue(object : Callback<TopRatedResponse> {
            override fun onResponse(
                call: Call<TopRatedResponse>,
                response: Response<TopRatedResponse>
            ) {
                return callback(response.body()!!.toprated)
            }

            override fun onFailure(call: Call<TopRatedResponse>, t: Throwable?) {

            }
        })
    }

    private fun getTvShowsList(callback: (List<TvShows>) -> Unit) {
        val apiService = TvShowsApiService.getInstance().create(TvShowsApiInterface::class.java)
        apiService.getTvShowsList().enqueue(object : Callback<TvShowsResponse> {
            override fun onResponse(
                call: Call<TvShowsResponse>,
                response: Response<TvShowsResponse>
            ) {
                return callback(response.body()!!.tvshows)
            }

            override fun onFailure(call: Call<TvShowsResponse>, t: Throwable?) {

            }
        })
    }
}