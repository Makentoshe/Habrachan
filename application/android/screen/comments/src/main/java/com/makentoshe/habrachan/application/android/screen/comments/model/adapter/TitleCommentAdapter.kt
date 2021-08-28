package com.makentoshe.habrachan.application.android.screen.comments.model.adapter

import android.view.ViewGroup
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.CommentAdapterController

class TitleCommentAdapter(
    private val commentAdapterController: CommentAdapterController
) : BaseCommentAdapter<CommentViewHolder>() {

    companion object : Analytics(LogAnalytic())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.Factory().build(parent.context, parent)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val model = getItem(position) ?: return capture(analyticEvent("Comment is null at position $position"))
        commentAdapterController.onBindViewHolderComment(holder, model)
    }

//    private fun onBindViewHolder(holder: CommentViewHolder, model: CommentModelElement) {
//        CommentViewController(holder).default(model.comment).setLevel(model.level).setCommentAvatar(holder, model)
//        CommentBodyController(holder).setContent(commentContentFactory.build(model.comment.message)).collapse()
//        CommentBottomPanelController(holder).hide()
//    }
}
