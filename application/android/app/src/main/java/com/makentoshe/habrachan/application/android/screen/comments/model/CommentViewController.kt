package com.makentoshe.habrachan.application.android.screen.comments.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.screen.comments.view.CommentViewHolder
import com.makentoshe.habrachan.entity.natives.Comment
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.image.network.OkHttpNetworkSchemeHandler
import okhttp3.OkHttpClient

class CommentViewController(private val holder: CommentViewHolder) {

    // TODO optimize image loading using image arena
    private val markwon = Markwon.builder(holder.context)
        .usePlugin(HtmlPlugin.create())
        .usePlugin(ImagesPlugin.create {
            it.addSchemeHandler(OkHttpNetworkSchemeHandler.create(OkHttpClient()))
        }).build()

    init {
        holder.levelView.removeAllViews()
        holder.voteScoreView.text = ""
        holder.authorView.text = ""
        holder.timestampView.text = ""
    }

    fun install(comment: Comment): CommentViewController {
        render(comment)
        setVoteScore(comment.score)
        setAuthor(comment.author.login)
        setTimestamp(comment.timePublished)
        return this
    }

    fun render(comment: Comment): CommentViewController {
        markwon.setMarkdown(holder.bodyView, comment.message)
        return this
    }

    fun setAuthor(author: String) : CommentViewController {
        holder.authorView.text = author
        return this
    }

    fun setTimestamp(timestamp: String) : CommentViewController {
        holder.timestampView.text = timestamp
        return this
    }

    fun setVoteListener(
        voteUpListener: (View) -> Unit,
        voteDownListener: (View) -> Unit
    ): CommentViewController {
        holder.voteUpView.setOnClickListener(voteUpListener)
        holder.voteDownView.setOnClickListener(voteDownListener)
        return this
    }

    fun setLevel(level: Int): CommentViewController {
        val inflater = LayoutInflater.from(holder.context)
        (0 until level).forEach { _ ->
            inflater.inflate(R.layout.layout_comment_level, holder.levelView, true)
        }
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

    fun setAvatar(bitmap: Bitmap) : CommentViewController {
        holder.avatarView.setImageBitmap(bitmap)
        return this
    }

    fun setAvatar(drawable: Drawable) : CommentViewController {
        Handler(Looper.getMainLooper()).post { holder.avatarView.setImageDrawable(drawable) }
        return this
    }
}