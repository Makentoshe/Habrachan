package com.makentoshe.habrachan.model.article

import android.content.Intent
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat

class HabrachanWebViewClient : WebViewClient() {

    private var listener: (() -> Unit)? = null

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        super.onPageCommitVisible(view, url)
        listener?.invoke()
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url
        val context = view?.context ?: return true
        val intent = Intent(Intent.ACTION_VIEW, url)
        ContextCompat.startActivity(context, intent, null)
        return true
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        super.onReceivedError(view, request, error)
        println(request)
    }

    fun onPublicationReadyToShow(listener: () -> Unit) {
        this.listener = listener
    }
}