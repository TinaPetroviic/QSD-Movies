package com.example.qsdmovies.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.databinding.ActivityWebviewhelpBinding

class WebViewHelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewhelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewhelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webViewHelp.webViewClient = WebViewClient()

        binding.webViewHelp.webViewClient = object : WebViewClient() {

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
        binding.webViewHelp.settings.javaScriptEnabled = true

        val settings = binding.webViewHelp.settings
        settings.domStorageEnabled = true

        binding.webViewHelp.loadUrl("https://www.themoviedb.org/faq/general")
    }
}