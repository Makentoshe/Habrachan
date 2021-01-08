package com.makentoshe.habrachan.application.android.screen.comments.model

import android.view.View
import com.makentoshe.habrachan.entity.Comment

class CommentViewController(private val holder: CommentViewHolder) {

    fun render(comment: Comment) {
        holder.authorView.text = comment.author.login
        holder.voteScoreView.text = comment.score.toString()
        holder.bodyView.text = comment.message
        holder.timestampView.text = comment.timePublished
    }

    fun setVoteListener(voteUpListener: (View) -> Unit, voteDownListener: (View) -> Unit) {
        holder.voteUpView.setOnClickListener(voteUpListener)
        holder.voteDownView.setOnClickListener(voteDownListener)
    }

    fun setVoteScore(score: Int) {
        holder.voteScoreView.text = score.toString()
    }
}