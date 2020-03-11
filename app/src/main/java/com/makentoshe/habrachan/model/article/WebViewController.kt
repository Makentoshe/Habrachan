package com.makentoshe.habrachan.model.article

import android.webkit.WebView
import android.webkit.WebViewClient
import com.makentoshe.habrachan.view.article.ArticleFragment

class WebViewController(private val fragment: ArticleFragment, private val javaScriptInterface: JavaScriptInterface) {

    private val webViewClient = object : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            fragment.onArticleDisplayed()
        }

        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            fragment.onArticleError(description ?: "Description is null. Error code: $errorCode")
        }
    }

    fun init(webView: WebView) {
        webView.webViewClient = webViewClient
        webView.addJavascriptInterface(javaScriptInterface, "JSInterface")
    }
}