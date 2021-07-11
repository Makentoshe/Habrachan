package com.makentoshe.habrachan.application.android.common.comment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
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
        setStubAvatar().setTimestamp(comment)
        setAuthor(comment.author.login)
        setVoteScore(comment.score)
        setContent(CommentContent.Factory(holder.context).build(comment.message))
//        navigator?.let { navigator ->
//            holder.itemView.setOnClickListener {
//                navigator.toDetailsScreen(comment.commentId)
//            }
//        }
        return this
    }

    fun setContent(content: CommentContent): CommentViewController {
        content.setContentToView(holder.bodyView)
        return this
    }

    fun setVoteScore(score: Int): CommentViewController {
        holder.voteScoreView.text = score.toString()
        return this
    }

    fun setStubAvatar(): CommentViewController {
        holder.avatarView.setImageResource(R.drawable.ic_account_stub)
        return this
    }

    fun setAvatar(bitmap: Bitmap): CommentViewController {
        holder.avatarView.setImageBitmap(bitmap)
        return this
    }

    fun setAvatar(drawable: Drawable): CommentViewController {
        holder.avatarView.setImageDrawable(drawable)
        return this
    }

    fun setLevel(level: Int): CommentViewController {
        val inflater = LayoutInflater.from(holder.context)
        (0 until level).forEach { _ ->
            inflater.inflate(R.layout.layout_comment_level, holder.levelView, true)
        }
        return this
    }

    fun setAuthor(author: String): CommentViewController {
        holder.authorView.text = author
        return this
    }

    fun setTimestamp(timestamp: String): CommentViewController {
        holder.timestampView.text = timestamp
        return this
    }

    fun setTimestamp(comment: Comment): CommentViewController {
        setTimestamp(comment.timePublished.time(holder.context, R.string.format_comment_time))
        return this
    }

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
