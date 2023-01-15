package com.example.qsdmovies.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qsdmovies.adapter.MovieAdapter
import com.example.qsdmovies.adapter.TvShowsAdapter
import com.example.qsdmovies.databinding.FragmentSearchBinding
import com.example.qsdmovies.models.Movie
import com.example.qsdmovies.models.MovieResponse
import com.example.qsdmovies.models.TvShows
import com.example.qsdmovies.models.TvShowsResponse
import com.example.qsdmovies.services.MovieApiInterface
import com.example.qsdmovies.services.MoviesApiService
import com.example.qsdmovies.services.TvShowsApiInterface
import com.example.qsdmovies.services.TvShowsApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewTvShows.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewTvShows.setHasFixedSize(true)
        getTvShowsList { tvshows: List<TvShows> ->
            binding.recyclerViewTvShows.adapter = TvShowsAdapter(tvshows)
        }

        binding.recyclerViewMovies.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewMovies.setHasFixedSize(true)
        getMovieData { movies: List<Movie> ->
            binding.recyclerViewMovies.adapter = MovieAdapter(movies)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

