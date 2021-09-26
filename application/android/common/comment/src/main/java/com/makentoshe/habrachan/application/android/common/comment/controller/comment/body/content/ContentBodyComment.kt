package com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content

import android.content.Context
import android.widget.TextView
import com.makentoshe.habrachan.application.android.common.comment.ImageClickMarkwonPlugin
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.ContentScreenNavigator
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.image.network.OkHttpNetworkSchemeHandler
import okhttp3.OkHttpClient
import javax.inject.Inject

class ContentBodyComment(val content: String, context: Context) {

    private var imageClickPlugin: ImageClickMarkwonPlugin? = null
    private val htmlPlugin = HtmlPlugin.create()
    private val imagePlugin = ImagesPlugin.create {
        // TODO optimize image loading using image arena
        it.addSchemeHandler(OkHttpNetworkSchemeHandler.create(OkHttpClient()))
    }

    private val markwonBuilder = Markwon.builder(context)

    fun setNavigationOnImageClick(navigator: ContentScreenNavigator): ContentBodyComment {
        imageClickPlugin = ImageClickMarkwonPlugin(navigator)
        return this
    }

    internal fun setContentToView(textView: TextView) {
        val list = mutableListOf(htmlPlugin, imagePlugin)
        imageClickPlugin?.let(list::add)
        markwonBuilder.usePlugins(list).build().setMarkdown(textView, content)
    }

    class Factory @Inject constructor(private val context: Context) {

        private var imageClickPlugin: ImageClickMarkwonPlugin? = null

        fun setNavigationOnImageClick(navigator: ContentScreenNavigator): Factory {
            imageClickPlugin = ImageClickMarkwonPlugin(navigator)
            return this
        }

        fun build(content: String) = ContentBodyComment(content, context).apply {
            this.imageClickPlugin = this@Factory.imageClickPlugin
        }
    }
}