package com.makentoshe.habrachan.model.post

import android.content.Context
import android.widget.Toast

class JavaScriptInterface(private var mContext: Context) {

    @android.webkit.JavascriptInterface
    fun showToast() {
        Toast.makeText(mContext, "Toast", Toast.LENGTH_LONG).show()
        println("SAS ASA ANUS PSA")
    }

    @android.webkit.JavascriptInterface
    fun showSpoilerInWindow(codeHtml: String) {
        Toast.makeText(mContext, codeHtml, Toast.LENGTH_LONG).show()
    }
}