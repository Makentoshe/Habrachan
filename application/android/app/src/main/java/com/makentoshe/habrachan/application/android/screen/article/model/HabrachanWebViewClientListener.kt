package com.makentoshe.habrachan.application.android.screen.article.model

import android.webkit.WebView

interface HabrachanWebViewClientListener {

    fun onWebReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?)

}