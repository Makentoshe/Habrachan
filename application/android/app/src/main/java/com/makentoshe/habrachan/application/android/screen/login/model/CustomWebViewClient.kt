package com.makentoshe.habrachan.application.android.screen.login.model

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.WebMobileLoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CustomWebViewClient(
    private val viewModel: WebMobileLoginViewModel, private val coroutineScope: CoroutineScope
) : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url ?: throw IllegalStateException()
        if (url.path.equals("/ac/entrance/")) {
            coroutineScope.launch { viewModel.webViewLoginUrlChannel.send(url.toString()) }
            return true
        } else {
            return false
        }
    }
}