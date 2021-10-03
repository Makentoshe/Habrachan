package com.makentoshe.habrachan.application.android.screen.article.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class JavaScriptInterface @Inject constructor(private val coroutineScope: CoroutineScope) {

    private val imageSourceChannel = Channel<String>()

    val imageSourceFlow = imageSourceChannel.receiveAsFlow()

    @android.webkit.JavascriptInterface
    fun onImageClickedListener(imageSource: String) = coroutineScope.launch {
        imageSourceChannel.send(imageSource)
    }
}