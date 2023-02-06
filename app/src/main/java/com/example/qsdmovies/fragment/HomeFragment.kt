package com.example.qsdmovies.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.map
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.qsdmovies.activity.MovieDetailsActivity
import com.example.qsdmovies.adapter.MovieAdapter
import com.example.qsdmovies.databinding.FragmentHomeBinding
import com.example.qsdmovies.models.Movie
import com.example.qsdmovies.util.Constants
import com.example.qsdmovies.util.observe
import com.example.qsdmovies.viewmodels.HomeViewModel
import timber.log.Timber

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private val movieAdapter = MovieAdapter {
        openMovieActivity(it)
    }
    private val topRatedAdapter = MovieAdapter {
        openMovieActivity(it)
    }
    private val tvShowsAdapter = MovieAdapter {
        openMovieActivity(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")

        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFlows()
        binding.rvWatching.adapter = movieAdapter
        binding.rvPopularMovies.adapter = movieAdapter
        binding.rvTopRated.adapter = topRatedAdapter
        binding.rvTelevisionShows.adapter = tvShowsAdapter
    }

    private fun initFlows() {
        viewModel.stateFlow.observe(viewLifecycleOwner) {
            movieAdapter.submitData(it)

        }

        viewModel.stateFlowTopRated.observe(viewLifecycleOwner) { it ->
            topRatedAdapter.submitData(it.map {
                Movie(
                    it.id?.toLong() ?: -1,
                    it.title ?: "",
                    it.overview,
                    it.poster ?: "",
                    it.vote.toFloat()
                )
            })
        }

        viewModel.stateFlowTvShows.observe(viewLifecycleOwner) {
            tvShowsAdapter.submitData(it)
        }

        viewModel.firstPosterLink.observe(viewLifecycleOwner) {
            it?.let { loadPoster(it) }
        }
    }

    private fun loadPoster(link: String) {
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w342$link")
            .transform(CenterCrop())
            .into(binding.ivPoster)
    }

    private fun openMovieActivity(model: Movie) {
        val intent = Intent(requireActivity(), MovieDetailsActivity::class.java).apply {
            putExtra(Constants.MOVIE_TITLE, model.title)
            putExtra(Constants.MOVIE_RATING, model.rating)
            putExtra(Constants.MOVIE_OVERVIEW, model.overview)
            putExtra(Constants.MOVIE_POSTER, model.poster)
            putExtra(Constants.MOVIE_ID, model.id.toInt())
        }

        requireActivity().startActivity(intent)
    }
}