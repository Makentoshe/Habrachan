package com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement

class ComposeCommentAdapterController(
    val avatarCommentAdapterController: AvatarCommentAdapterController,
    val bodyCommentAdapterController: BodyCommentAdapterController,
    val panelCommentAdapterController: PanelCommentAdapterController,
) : CommentAdapterController {

    override fun onBindViewHolderComment(holder: CommentViewHolder, model: CommentModelElement) {
        bodyCommentAdapterController.onBindViewHolderComment(holder, model)
        avatarCommentAdapterController.onBindViewHolderComment(holder, model)
        panelCommentAdapterController.onBindViewHolderComment(holder, model)
    }
}