package com.example.qsdmovies.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.databinding.ActivityMoviedetailsBinding

class MovieDetails : AppCompatActivity() {

    private lateinit var binding: ActivityMoviedetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviedetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "BACK"

        binding.watchNowButton.setOnClickListener {
            val intent = Intent(this, WebViewWatchNowActivity::class.java)
            startActivity(intent)
        }
    }
}