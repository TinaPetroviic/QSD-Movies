package com.example.qsdmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.qsdmovies.adapter.MovieAdapter
import com.example.qsdmovies.databinding.FragmentSearchBinding
import com.example.qsdmovies.models.Movie
import com.example.qsdmovies.models.MovieResponse
import com.example.qsdmovies.models.TvShows
import com.example.qsdmovies.models.TvShowsResponse
import com.example.qsdmovies.network.NetworkCore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTvShowsList { tvShows: List<TvShows> ->
            binding.rvTelevisionShows.adapter = MovieAdapter(tvShows.map { it.poster })
        }
        getMovieData { movies: List<Movie> ->
            binding.rvMovies.adapter = MovieAdapter(movies.map { it.poster })
        }
    }

    private fun getMovieData(callback: (List<Movie>) -> Unit) {
        NetworkCore.moviesAPI.getMovieList().enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Timber.e(t)
            }
        })
    }

    private fun getTvShowsList(callback: (List<TvShows>) -> Unit) {
        NetworkCore.moviesAPI.getTvShowsList().enqueue(object : Callback<TvShowsResponse> {
            override fun onResponse(
                call: Call<TvShowsResponse>,
                response: Response<TvShowsResponse>
            ) {
                return callback(response.body()!!.tvShows)
            }

            override fun onFailure(call: Call<TvShowsResponse>, t: Throwable) {
                Timber.e(t)
            }
        })
    }
}

