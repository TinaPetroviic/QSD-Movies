package com.example.qsdmovies.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.databinding.ActivityCastBinding

class CastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCastBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}