package com.example.qsdmovies.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.qsdmovies.databinding.ActivityMoviedetailsBinding
import com.example.qsdmovies.util.Constants.MOVIE_OVERVIEW
import com.example.qsdmovies.util.Constants.MOVIE_POSTER
import com.example.qsdmovies.util.Constants.MOVIE_RATING
import com.example.qsdmovies.util.Constants.MOVIE_TITLE
import kotlinx.android.synthetic.main.activity_moviedetails.*

class MovieDetails : AppCompatActivity() {

    private lateinit var binding: ActivityMoviedetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviedetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnWatchNow.setOnClickListener {
            val intent = Intent(this, WebViewWatchNowActivity::class.java)
            startActivity(intent)
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

        movie_title.text = extras.getString(MOVIE_TITLE, "")
        movie_rating.rating = extras.getFloat(MOVIE_RATING, 0f) / 2
        movie_overview.text = extras.getString(MOVIE_OVERVIEW, "")
    }
}

