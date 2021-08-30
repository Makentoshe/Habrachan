package com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content.ContentBodyComment
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement

class BodyCommentAdapterController(
    private val contentBodyCommentFactory: ContentBodyComment.Factory,
    private val installWizard: InstallWizard = InstallWizard()
) : CommentAdapterController {

    override fun onBindViewHolderComment(holder: CommentViewHolder, model: CommentModelElement) {
        val commentViewController = CommentViewController(holder).apply { dispose() }
        commentViewController.installBodyState(installWizard)

        commentViewController.body.avatar.setStubAvatar()
        commentViewController.body.author.setAuthor(model.comment)
        commentViewController.body.timestamp.setTimestamp(model.comment)
        commentViewController.body.level.setLevel(model.level)
        commentViewController.body.content.setContent(contentBodyCommentFactory.build(model.comment.message))
    }

    private fun CommentViewController.installBodyState(installWizard: InstallWizard) = when (installWizard.bodyState) {
        InstallWizard.BodyState.EXPANDED -> body.expand()
        InstallWizard.BodyState.COLLAPSED -> body.collapse()
    }

    class InstallWizard(var bodyState: BodyState = BodyState.EXPANDED) {

        enum class BodyState {
            COLLAPSED, EXPANDED
        }
    }
}
