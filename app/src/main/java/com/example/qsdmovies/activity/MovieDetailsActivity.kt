package com.example.qsdmovies.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.qsdmovies.adapter.CastAdapter
import com.example.qsdmovies.databinding.ActivityMoviedetailsBinding
import com.example.qsdmovies.util.Constants
import com.example.qsdmovies.util.Constants.MOVIE_OVERVIEW
import com.example.qsdmovies.util.Constants.MOVIE_POSTER
import com.example.qsdmovies.util.Constants.MOVIE_RATING
import com.example.qsdmovies.util.Constants.MOVIE_TITLE
import com.example.qsdmovies.viewmodels.MovieDetailsViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoviedetailsBinding
    private var castAdapter = CastAdapter(listOf()) {

    }
    private lateinit var viewModel: MovieDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviedetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MovieDetailsViewModel::class.java]
        setupCasts()

        binding.clBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnWatchNow.setOnClickListener {
            val intent = Intent(this, WebViewWatchNowActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            observeCasts()
        }

        val extras = intent.extras

        if (extras != null) {
            populateDetails(extras)
        } else {
            finish()
        }
    }

    private fun populateDetails(extras: Bundle) {

        extras.getString(MOVIE_POSTER)?.let { posterPath ->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w342$posterPath")
                .transform(CenterCrop())
                .into(binding.moviePoster)
        }

        binding.movieTitle.text = extras.getString(MOVIE_TITLE, "")
        binding.movieRating.rating = extras.getFloat(MOVIE_RATING, 0f) / 2
        binding.movieRatingValue.text = (extras.getFloat(MOVIE_RATING, 0f) / 2).toString()
        binding.movieOverview.text = extras.getString(MOVIE_OVERVIEW, "")
        val movieId = extras.getInt(Constants.MOVIE_ID)
        viewModel.getCasts(movieId)
    }

    private fun setupCasts() {
        binding.rvCast.adapter = castAdapter
    }

    private suspend fun observeCasts() {
        viewModel.casts
            .mapNotNull { it }
            .distinctUntilChanged()
            .collect {
                castAdapter = CastAdapter(it) { crew ->

                }
                binding.rvCast.adapter = castAdapter
            }
    }
}

