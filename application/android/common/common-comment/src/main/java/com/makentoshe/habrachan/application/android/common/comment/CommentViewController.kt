@file:Suppress("UsePropertyAccessSyntax")

package com.makentoshe.habrachan.application.android.common.comment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.makentoshe.habrachan.application.android.common.core.dp2px
import com.makentoshe.habrachan.application.android.common.core.time
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.entity.timePublished
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.image.network.OkHttpNetworkSchemeHandler
import okhttp3.OkHttpClient

class CommentViewController(private val holder: CommentViewHolder) {

    init {
        holder.levelView.removeAllViews()
        holder.voteScoreView.text = ""
        holder.authorView.text = ""
        holder.timestampView.text = ""
    }

    fun default(comment: Comment): CommentViewController {
        setStubAvatar()
        setTimestamp(comment)
        setAuthor(comment)
        setVoteScore(comment)
        return this
    }

    fun setContent(content: CommentContent) = content.setContentToView(holder.bodyView)

    fun setContent(comment: Comment) = setContent(CommentContent.Factory(holder.context).build(comment.message))

    fun setVoteScore(score: Int) = holder.voteScoreView.setText(score.toString())

    fun setVoteScore(comment: Comment) = setVoteScore(comment.score)

    fun setStubAvatar() = holder.avatarView.setImageResource(R.drawable.ic_account_stub)

    fun setAvatar(bitmap: Bitmap) = holder.avatarView.setImageBitmap(bitmap)

    fun setAvatar(drawable: Drawable)= holder.avatarView.setImageDrawable(drawable)

    fun setLevel(level: Int): CommentViewController {
        val inflater = LayoutInflater.from(holder.context)
        (0 until level).forEach { _ ->
            inflater.inflate(R.layout.layout_comment_level, holder.levelView, true)
        }
        return this
    }

    fun setAuthor(author: String) = holder.authorView.setText(author)

    fun setAuthor(comment: Comment) = setAuthor(comment.author.login)

    fun setTimestamp(timestamp: String) = holder.timestampView.setText(timestamp)

    fun setTimestamp(comment: Comment) = setTimestamp(comment.timePublished.time(holder.context, R.string.format_comment_time))

    fun setVoteUpAction(listener: () -> Unit) {
        holder.voteUpView.setOnClickListener { listener() }
    }

    fun setVoteDownAction(listener: () -> Unit) {
        holder.voteDownView.setOnClickListener { listener() }
    }

    fun setReplyAction(listener: () -> Unit) {
        holder.replyView.setOnClickListener { listener() }
    }

    fun setShareAction(listener: () -> Unit) {
        holder.shareView.setOnClickListener { listener() }
    }

    fun setBookmarkAction(listener: () -> Unit) {
        holder.bookmarkView.setOnClickListener { listener() }
    }

    fun setOverflowAction(listener: () -> Unit) {
        holder.overflowView.setOnClickListener { listener() }
    }

    fun showExpandedBottomPanel() {
        showCollapsedBottomPanel()
        holder.replyView.visibility = View.VISIBLE
        holder.shareView.visibility = View.VISIBLE
        holder.bookmarkView.visibility = View.VISIBLE
    }

    fun showCollapsedBottomPanel() {
        holder.voteDownView.visibility = View.VISIBLE
        holder.voteUpView.visibility = View.VISIBLE
        holder.voteScoreView.visibility = View.VISIBLE
        holder.overflowView.visibility = View.VISIBLE
    }

    fun hideBottomPanel() {
        holder.voteDownView.visibility = View.GONE
        holder.voteUpView.visibility = View.GONE
        holder.voteScoreView.visibility = View.GONE
        holder.overflowView.visibility = View.GONE
        holder.replyView.visibility = View.GONE
        holder.shareView.visibility = View.GONE
        holder.bookmarkView.visibility = View.GONE
    }

    fun collapseCommentBody() = with(holder.bodyView) {
        maxHeight = holder.context.dp2px(100f).toInt()
        isEnabled = false
    }

    fun expandCommentBody() = with (holder.bodyView) {
        maxHeight = Int.MAX_VALUE
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
