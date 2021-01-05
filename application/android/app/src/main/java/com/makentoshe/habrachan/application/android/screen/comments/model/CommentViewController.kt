package com.makentoshe.habrachan.application.android.screen.comments.model

import android.view.View
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.screen.comments.view.CommentViewHolder
import com.makentoshe.habrachan.entity.Comment

class CommentViewController(private val holder: CommentViewHolder) {

    fun render(comment: Comment) {
        holder.authorView.text = comment.author.login
        holder.voteScoreView.text = comment.score.toString()
        holder.bodyView.text = comment.message
        holder.timestampView.text = comment.timePublished
    }

    fun setReplies(replies: Int, clickListener: (View) -> Unit) {
        holder.replyTextView.setOnClickListener(clickListener)
        holder.replyTextView.text = if (replies == 0) {
            holder.context.getString(R.string.comments_reply_empty)
        } else {
            holder.context.getString(R.string.comments_reply_count, replies)
        }
    }

    fun setVoteListener(voteUpListener: (View) -> Unit, voteDownListener: (View) -> Unit) {
        holder.voteUpView.setOnClickListener(voteUpListener)
        holder.voteDownView.setOnClickListener(voteDownListener)
    }

    fun setVoteScore(score: Int) {
        holder.voteScoreView.text = score.toString()
    }
}