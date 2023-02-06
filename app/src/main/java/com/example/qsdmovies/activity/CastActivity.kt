package com.example.qsdmovies.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.qsdmovies.databinding.ActivityCastBinding
import com.example.qsdmovies.util.Constants

class CastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        intent.extras?.let {
            populateDetails(it)
        }
    }

    private fun populateDetails(extras: Bundle) {

        extras.getString(Constants.CREW_IMAGE)?.let { posterPath ->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w342$posterPath")
                .transform(CenterCrop())
                .into(binding.imgCast)
        }

        binding.tvName.text = extras.getString(Constants.CREW_DESC)
    }
}