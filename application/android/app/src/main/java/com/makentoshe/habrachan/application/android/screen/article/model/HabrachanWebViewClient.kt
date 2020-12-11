package com.makentoshe.habrachan.application.android.screen.article.model

import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat

class HabrachanWebViewClient(private val listener: HabrachanWebViewClientListener) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url
        val context = view?.context ?: return true
        val intent = Intent(Intent.ACTION_VIEW, url)
        ContextCompat.startActivity(context, intent, null)
        return true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        listener.onWebPageFinished(view, url)
    }

    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        listener.onWebReceivedError(view, errorCode, description, failingUrl)
    }
}