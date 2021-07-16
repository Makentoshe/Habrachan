package com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller

import com.makentoshe.habrachan.application.android.common.comment.CommentBodyController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentModelElement

class ContentCommentAdapterController(
    private val commentAdapterController: CommentAdapterController?,
    private val commentContentFactory: CommentBodyController.CommentContent.Factory,
    private val additional: (CommentViewController, CommentBodyController) -> Unit = { _, _ -> }
) : CommentAdapterController {

    override fun onBindViewHolderComment(holder: CommentViewHolder, position: Int, model: CommentModelElement) {
        commentAdapterController?.onBindViewHolderComment(holder, position, model)

        val viewController = CommentViewController(holder).dispose().default(model.comment).setLevel(model.level)
        val bodyController = CommentBodyController(holder).setContent(commentContentFactory.build(model.comment.message))
        additional(viewController, bodyController)
    }
}