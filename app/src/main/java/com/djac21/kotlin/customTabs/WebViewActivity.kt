package com.djac21.kotlin.customTabs

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.graphics.Bitmap
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.widget.ProgressBar
import com.djac21.kotlin.R

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var mProgressLoading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val url = intent.getStringExtra("extra.url")

        webView = findViewById(R.id.webview_content)
        mProgressLoading = findViewById(R.id.progress_loading)

        webView.webViewClient = WebViewClient()
//        webView.webChromeClient = WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                setPageLoadProgress(newProgress)
//                super.onProgressChanged(view, newProgress)
//            }
//        }
//        WebSettings webSettings = webView.getSettings()
//        webSettings.setJavaScriptEnabled(true)
        webView.loadUrl(url)

        title = url
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setPageLoadProgress(progress: Int) {
        mProgressLoading.setProgress(progress)
    }

    private fun showProgress() {
        mProgressLoading.setVisibility(View.VISIBLE)
    }

    private fun hideProgress() {
        mProgressLoading.setVisibility(View.GONE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class WebViewClient : android.webkit.WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            showProgress()
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView, url: String) {
            hideProgress()
            super.onPageFinished(view, url)
        }
    }
}