package com.example.qsdmovies.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.qsdmovies.adapter.MovieAdapter
import com.example.qsdmovies.adapter.TopRatedAdapter
import com.example.qsdmovies.adapter.TvShowsAdapter
import com.example.qsdmovies.databinding.FragmentHomeBinding
import com.example.qsdmovies.models.*
import com.example.qsdmovies.services.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("HomeFragment", "onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewWatching.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewWatching.setHasFixedSize(true)
        getMovieData { movies: List<Movie> ->
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + movies.first().poster)
                .into(binding.poster)
            binding.recyclerViewWatching.adapter = MovieAdapter(movies)
        }

        binding.recyclerViewTopRated.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewTopRated.setHasFixedSize(true)
        getTopRatedList { toprated: List<TopRated> ->
            binding.recyclerViewTopRated.adapter = TopRatedAdapter(toprated)
        }

        binding.recyclerViewTVShows.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewTVShows.setHasFixedSize(true)
        getTvShowsList { tvshows: List<TvShows> ->
            binding.recyclerViewTVShows.adapter = TvShowsAdapter(tvshows)
        }

        binding.recyclerViewPopularMovies.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPopularMovies.setHasFixedSize(true)
        getMovieData { movies: List<Movie> ->
            binding.recyclerViewPopularMovies.adapter = MovieAdapter(movies)
        }
    }

    private fun getMovieData(callback: (List<Movie>) -> Unit) {
        val apiService = MoviesApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

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

            override fun onFailure(call: Call<TopRatedResponse>, t: Throwable) {

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

            override fun onFailure(call: Call<TvShowsResponse>, t: Throwable) {

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}