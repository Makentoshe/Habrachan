package com.makentoshe.habrachan.model.post

import ru.terrakok.cicerone.Router

class JavaScriptInterface(private val router: Router) {

    @android.webkit.JavascriptInterface
    fun showToast() {
        println("SAS ASA ANUS PSA")
    }

    @android.webkit.JavascriptInterface
    fun showSpoilerInWindow(codeHtml: String) = Unit
}