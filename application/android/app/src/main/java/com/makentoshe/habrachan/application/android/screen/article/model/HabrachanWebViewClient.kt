package com.makentoshe.habrachan.application.android.screen.article.model

import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment

class HabrachanWebViewClient(private val fragment: ArticleFragment) : WebViewClient() {

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

    override fun onPageFinished(view: WebView?, url: String?) {
        fragment.onArticleDisplayed()
    }

    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        fragment.onArticleReceivedError(description ?: "Description is null. Error code: $errorCode")
    }

    fun onPublicationReadyToShow(listener: () -> Unit) {
        this.listener = listener
    }
}