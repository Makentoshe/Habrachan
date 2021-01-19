package com.makentoshe.habrachan.application.android.screen.comments.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.screen.comments.view.CommentViewHolder
import com.makentoshe.habrachan.entity.Comment

class CommentViewController(private val holder: CommentViewHolder) {

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
        holder.bodyView.text = comment.message
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