package com.example.qsdmovies.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.qsdmovies.databinding.ActivityWebviewwatchBinding
import kotlinx.android.synthetic.main.activity_webviewhelp.*

class WebViewWatchNowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebviewwatchBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webViewWatchNow.webViewClient = WebViewClient()

        web_view_help.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view?.visibility = View.INVISIBLE
                progress_bar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.visibility = View.VISIBLE
                progress_bar.visibility = View.INVISIBLE
            }

        }
        web_view_help.settings.javaScriptEnabled = true

        val settings = web_view_help.settings
        settings.domStorageEnabled = true

        web_view_help.loadUrl("https://www.themoviedb.org/movie")
    }
}
