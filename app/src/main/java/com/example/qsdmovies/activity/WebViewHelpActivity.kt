package com.example.qsdmovies.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.databinding.ActivityWebviewhelpBinding

class WebViewHelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewhelpBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewhelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "BACK"

        binding.webViewHelp.webViewClient = WebViewClient()

        binding.webViewHelp.apply {
            loadUrl("https://www.themoviedb.org/faq/general")
            settings.javaScriptEnabled = true
        }
    }
}