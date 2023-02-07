package com.example.qsdmovies.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.qsdmovies.activity.MovieDetailsActivity
import com.example.qsdmovies.adapter.MovieAdapter
import com.example.qsdmovies.databinding.FragmentSearchBinding
import com.example.qsdmovies.models.Movie
import com.example.qsdmovies.util.Constants
import com.example.qsdmovies.util.afterTextChangedDebounce
import com.example.qsdmovies.util.observe
import com.example.qsdmovies.viewmodels.SearchViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchViewModel
    private var movieAdapter = MovieAdapter(
        onMovieTap = { openMovieActivity(it) },
        onLikeTap = { tapped -> })
    private var tvShowsAdapter = MovieAdapter(
        onMovieTap = { openMovieActivity(it) },
        onLikeTap = { tapped -> })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
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

        setupView()
        viewModel.stateFlow.observe(viewLifecycleOwner) {
            movieAdapter.submitData(it)
        }
        viewModel.stateFlowTvShows.observe(viewLifecycleOwner) {
            tvShowsAdapter.submitData(it)
        }
    }

    private fun setupView() {
        binding.search.afterTextChangedDebounce(1000L) {
            if (it.isBlank()) triggerSearch("a") else triggerSearch(it)
        }

        binding.rvMovies.adapter = movieAdapter
        binding.rvTelevisionShows.adapter = tvShowsAdapter
    }

    private fun triggerSearch(query: String) {
        viewModel.search(query).observe(viewLifecycleOwner) {
            movieAdapter.submitData(it)
        }

        viewModel.searchShows(query).observe(viewLifecycleOwner) {
            tvShowsAdapter.submitData(it)
        }
    }

    private fun openMovieActivity(model: Movie) {
        val intent = Intent(requireActivity(), MovieDetailsActivity::class.java).apply {
            putExtra(Constants.MOVIE_TITLE, model.title)
            putExtra(Constants.MOVIE_RATING, model.vote_average)
            putExtra(Constants.MOVIE_OVERVIEW, model.overview)
            putExtra(Constants.MOVIE_POSTER, model.poster_path)
            putExtra(Constants.MOVIE_ID, model.id.toInt())
        }

        requireActivity().startActivity(intent)
    }
}


