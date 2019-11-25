package com.makentoshe.habrachan.model.post

import android.content.Context

class JavaScriptInterface(private val context: Context) {

    @android.webkit.JavascriptInterface
    fun showToast() {
        println("SAS ASA ANUS PSA")
    }

    @android.webkit.JavascriptInterface
    fun onImageClickedListener(imageSource: String, imageSourcesString: String) {
        val imageSources = imageSourcesString.trim().split(" ")
        PostBroadcastReceiver.sendImageClickedBroadcast(context, imageSource, imageSources)
    }
}