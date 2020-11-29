package com.makentoshe.habrachan.view.comments.controller

import android.content.Context
import android.widget.TextView
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImagesPlugin

class NativeCommentTextController(private val textView: TextView) {

    fun setCommentText(commentHtml: String) {
        buildMarkwon(textView.context).setMarkdown(textView, commentHtml)
    }

    private fun buildMarkwon(context: Context): Markwon {
        val markwonBuilder = Markwon.builder(context)
        markwonBuilder.usePlugin(HtmlPlugin.create())
        markwonBuilder.usePlugin(ImagesPlugin.create())
        return markwonBuilder.build()
    }

    class Factory {
        fun build(textView: TextView) = NativeCommentTextController(textView)
    }
}

