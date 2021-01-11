package com.makentoshe.habrachan.application.android.screen.comments.model

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

    fun render(comment: Comment): CommentViewController {
        holder.authorView.text = comment.author.login
        holder.timestampView.text = comment.timePublished
        holder.bodyView.text = comment.message
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
}