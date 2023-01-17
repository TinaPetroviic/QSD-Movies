package com.example.qsdmovies.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.databinding.ActivityWebviewwatchBinding

class WebViewWatchNowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewwatchBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "BACK"

        binding.webViewWatchNow.webViewClient = WebViewClient()

        binding.webViewWatchNow.apply {
            loadUrl("https://www.themoviedb.org/movie")
            settings.javaScriptEnabled = true
        }
    }
}