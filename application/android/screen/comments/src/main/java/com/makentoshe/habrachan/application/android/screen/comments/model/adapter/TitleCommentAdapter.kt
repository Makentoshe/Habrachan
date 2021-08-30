package com.makentoshe.habrachan.application.android.screen.comments.model.adapter

import android.view.ViewGroup
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.CommentAdapterController
import javax.inject.Inject
import javax.inject.Named

class TitleCommentAdapter @Inject constructor(
    @Named("TitleCommentAdapterController")
    private val commentAdapterController: CommentAdapterController,
) : BaseCommentAdapter<CommentViewHolder>() {

    companion object : Analytics(LogAnalytic())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.Factory().build(parent.context, parent)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val model = getItem(position) ?: return capture(analyticEvent("Comment is null at position $position"))
        commentAdapterController.onBindViewHolderComment(holder, model)
    }
}
