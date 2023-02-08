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
import com.example.qsdmovies.adapter.SimpleMovieAdapter
import com.example.qsdmovies.databinding.FragmentHomeBinding
import com.example.qsdmovies.models.Movie
import com.example.qsdmovies.util.Constants
import com.example.qsdmovies.util.observe
import com.example.qsdmovies.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: HomeViewModel

    private val movieAdapter = SimpleMovieAdapter(
        list = listOf(),
        onMovieTap = { openMovieActivity(it) },
        onLikeTap = { movie -> handleFavorites(movie) })
    private val topRatedAdapter = MovieAdapter(
        onMovieTap = { openMovieActivity(it) },
        onLikeTap = { movie -> handleFavorites(movie) })
    private val tvShowsAdapter = MovieAdapter(
        onMovieTap = { openMovieActivity(it) },
        onLikeTap = { movie -> handleFavorites(movie) })
    private val popularAdapter = MovieAdapter(
        onMovieTap = { openMovieActivity(it) },
        onLikeTap = { movie -> handleFavorites(movie) })

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
        binding.rvPopularMovies.adapter = popularAdapter
        binding.rvTopRated.adapter = topRatedAdapter
        binding.rvTelevisionShows.adapter = tvShowsAdapter
    }

    private fun initFlows() {
        viewModel.stateFlow.observe(viewLifecycleOwner) {
            movieAdapter.setMovies(it)
        }

        viewModel.stateFlowTopRated.observe(viewLifecycleOwner) { it ->
            topRatedAdapter.submitData(it.map { topRated ->
                Movie(
                    topRated.id?.toLong() ?: -1,
                    topRated.title ?: "",
                    topRated.overview,
                    topRated.poster ?: "",
                    topRated.vote.toFloat()
                ).also { it.favorite = topRated.favorite }
            })
        }

        viewModel.stateFlowTvShows.observe(viewLifecycleOwner) {
            tvShowsAdapter.submitData(it)
        }

        viewModel.firstPosterLink.observe(viewLifecycleOwner) {
            it?.let { loadPoster(it) }
        }

        viewModel.stateFlowMovies.observe(viewLifecycleOwner) {
            popularAdapter.submitData(it)
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
            putExtra(Constants.MOVIE_RATING, model.vote_average)
            putExtra(Constants.MOVIE_OVERVIEW, model.overview)
            putExtra(Constants.MOVIE_POSTER, model.poster_path)
            putExtra(Constants.MOVIE_ID, model.id.toInt())
        }

        requireActivity().startActivity(intent)
    }

    private fun handleFavorites(model: Movie) {
        val db = Firebase.database
        val result = db.getReference(Constants.FAVORITES)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child(model.id.toString())

        if (model.favorite) result.setValue(model) else result.removeValue()
    }

}

