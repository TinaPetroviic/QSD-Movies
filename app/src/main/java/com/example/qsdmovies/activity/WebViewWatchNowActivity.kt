package com.example.qsdmovies.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.databinding.ActivityWebviewwatchBinding

class WebViewWatchNowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewwatchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webViewWatchNow.webViewClient = WebViewClient()

        binding.webViewWatchNow.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view?.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
            }

        }
        binding.webViewWatchNow.settings.javaScriptEnabled = true

        val settings = binding.webViewWatchNow.settings
        settings.domStorageEnabled = true

        binding.webViewWatchNow.loadUrl("https://www.themoviedb.org/movie")
    }
}


