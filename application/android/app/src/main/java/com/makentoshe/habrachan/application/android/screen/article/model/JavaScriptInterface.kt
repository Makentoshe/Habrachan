package com.makentoshe.habrachan.application.android.screen.article.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class JavaScriptInterface(private val coroutineScope: CoroutineScope) {

    val imageSourceChannel = Channel<String>()

    @android.webkit.JavascriptInterface
    fun showToast() {
        println("SAS ASA ANUS PSA")
    }

    @android.webkit.JavascriptInterface
    fun onImageClickedListener(imageSource: String) = coroutineScope.launch {
        imageSourceChannel.send(imageSource)
    }
}