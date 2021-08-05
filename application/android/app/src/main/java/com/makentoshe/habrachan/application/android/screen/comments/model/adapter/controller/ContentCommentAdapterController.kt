package com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content.ContentBodyComment
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentModelElement

class ContentCommentAdapterController(
    private val commentAdapterController: CommentAdapterController?,
    private val commentContentFactory: ContentBodyComment.Factory,
    private val additional: (CommentViewController) -> Unit = { _ -> }
) : CommentAdapterController {

    override fun onBindViewHolderComment(holder: CommentViewHolder, position: Int, model: CommentModelElement) {
        commentAdapterController?.onBindViewHolderComment(holder, position, model)
        CommentViewController(holder).setup(model).apply(additional)
    }

    private fun CommentViewController.setup(model: CommentModelElement) = with(this) {
        dispose()
        panel.vote.voteScore.setVoteScore(model.comment)
        panel.vote.setCurrentVoteState(model.comment)
        body.avatar.setStubAvatar()
        body.author.setAuthor(model.comment)
        body.timestamp.setTimestamp(model.comment)
        body.level.setLevel(model.level)
        body.content.setContent(commentContentFactory.build(model.comment.message))
        return@with this
    }
}