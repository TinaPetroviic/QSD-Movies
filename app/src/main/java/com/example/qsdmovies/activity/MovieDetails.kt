package com.example.qsdmovies.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.databinding.ActivityMoviedetailsBinding

class MovieDetails : AppCompatActivity() {

    private val TAG = "MovieDetails"
    private lateinit var binding: ActivityMoviedetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviedetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.d(TAG, "onCreate")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "BACK"


    }
}