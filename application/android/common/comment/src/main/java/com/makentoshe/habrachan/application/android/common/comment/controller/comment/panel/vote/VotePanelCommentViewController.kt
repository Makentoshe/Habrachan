package com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.vote

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.vote.down.DownVotePanelCommentViewControllerController
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.vote.score.ScoreVotePanelCommentViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.vote.up.UpVotePanelCommentViewController
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.entity.CommentVote
import com.makentoshe.habrachan.entity.vote

class VotePanelCommentViewController internal constructor(private val holder: CommentViewHolder) {

    val voteUp by lazy { UpVotePanelCommentViewController(holder) }

    val voteDown by lazy { DownVotePanelCommentViewControllerController(holder) }

    val voteScore by lazy { ScoreVotePanelCommentViewController(holder) }

    fun setCurrentVoteState(comment: Comment) = setCurrentVoteState(comment.vote)

    fun setCurrentVoteState(vote: CommentVote?) = when (vote) {
        CommentVote.Up -> voteUp.setVoteUpIcon()
        CommentVote.Down -> voteDown.setVoteDownIcon()
        else -> Unit
    }

    fun dispose() {
        voteScore.dispose()
        voteDown.resetVoteDownIcon()
        voteUp.resetVoteUpIcon()
    }
}