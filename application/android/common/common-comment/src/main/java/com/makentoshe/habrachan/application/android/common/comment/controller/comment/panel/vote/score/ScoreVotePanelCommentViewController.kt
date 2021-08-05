package com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.vote.score

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.entity.Comment

class ScoreVotePanelCommentViewController internal constructor(private val holder: CommentViewHolder) {

    fun setVoteScore(score: Int) = this.apply {
        holder.voteScoreView.setText(score.toString())
    }

    fun setVoteScore(comment: Comment) = setVoteScore(comment.score)

    fun dispose() = holder.voteScoreView.setText("")
}