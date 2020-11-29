package com.makentoshe.habrachan.model.article.web

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.makentoshe.habrachan.view.article.WebArticleFragment

class WebViewController(
    private val fragment: WebArticleFragment,
    private val javaScriptInterface: JavaScriptInterface
) {

    private val webViewClient = object : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            fragment.onArticleDisplayed()
        }

        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            fragment.onArticleError(description ?: "Description is null. Error code: $errorCode")
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return true
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun init(webView: WebView) {
        webView.webViewClient = webViewClient
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(javaScriptInterface, "JSInterface")
    }
}