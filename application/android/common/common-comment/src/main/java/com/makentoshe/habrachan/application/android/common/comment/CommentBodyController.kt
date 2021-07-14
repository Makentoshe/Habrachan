package com.makentoshe.habrachan.application.android.common.comment

import android.content.Context
import android.view.View
import android.widget.TextView
import com.makentoshe.habrachan.application.android.common.core.dp2px
import com.makentoshe.habrachan.entity.Comment
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.image.network.OkHttpNetworkSchemeHandler
import okhttp3.OkHttpClient

// TODO(medium) fix fade displaying for images
class CommentBodyController(private val holder: CommentViewHolder) {

    private val maxCollapsedHeight = holder.context.dp2px(100f).toInt()
    private val maxExpandedHeight = Int.MAX_VALUE

    init {
        holder.bodyView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
//            println("${holder.bodyView.height} ${holder.bodyView.maxHeight}")
            holder.fadeView.visibility = if (holder.bodyView.height == holder.bodyView.maxHeight) View.VISIBLE else View.GONE
        }
    }

    fun setContent(content: CommentContent): CommentBodyController {
        content.setContentToView(holder.bodyView)
        return this
    }

    fun setContent(comment: Comment) = setContent(CommentContent.Factory(holder.context).build(comment.message))

    fun collapse() = with(holder.bodyView) {
        maxHeight = maxCollapsedHeight
        isEnabled = false
    }

    fun expand() = with(holder.bodyView) {
        maxHeight = maxExpandedHeight
        isEnabled = true
    }

    class CommentContent(val content: String, context: Context) {

        private var imageClickPlugin: ImageClickMarkwonPlugin? = null
        private val htmlPlugin = HtmlPlugin.create()
        private val imagePlugin = ImagesPlugin.create {
            // TODO optimize image loading using image arena
            it.addSchemeHandler(OkHttpNetworkSchemeHandler.create(OkHttpClient()))
        }

        private val markwonBuilder = Markwon.builder(context)

        fun setNavigationOnImageClick(navigation: CommentViewControllerNavigator): CommentContent {
            imageClickPlugin = ImageClickMarkwonPlugin(navigation)
            return this
        }

        internal fun setContentToView(textView: TextView) {
            val list = mutableListOf(htmlPlugin, imagePlugin)
            imageClickPlugin?.let(list::add)
            markwonBuilder.usePlugins(list).build().setMarkdown(textView, content)
        }

        class Factory(private val context: Context) {

            private var imageClickPlugin: ImageClickMarkwonPlugin? = null

            fun setNavigationOnImageClick(navigation: CommentViewControllerNavigator): Factory {
                imageClickPlugin = ImageClickMarkwonPlugin(navigation)
                return this
            }

            fun build(content: String) = CommentContent(content, context).apply {
                this.imageClickPlugin = this@Factory.imageClickPlugin
            }
        }
    }
}