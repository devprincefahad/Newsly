package com.example.newsly

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.RelativeLayout

class DetailActivity : AppCompatActivity() {

    lateinit var detailWebView: WebView
    lateinit var progressBar: ProgressBar
    lateinit var container2: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detailWebView = findViewById(R.id.detailWebView)
        progressBar = findViewById(R.id.progressBar)
        //container2 = findViewById(R.id.container2)

        //container2.setBackgroundColor(Color.parseColor(ColorPicker.getColor()))

        val url = intent.getStringExtra("URL")
        if (url != null) {
            detailWebView.settings.javaScriptEnabled = true
            detailWebView.settings.userAgentString =
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
            detailWebView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE
                    detailWebView.visibility = View.VISIBLE
                }
            }
            detailWebView.loadUrl(url)
        }
    }
}