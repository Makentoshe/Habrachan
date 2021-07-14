package com.makentoshe.habrachan.application.android.screen.comments.model

import android.view.ViewGroup
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.comment.CommentBodyController
import com.makentoshe.habrachan.application.android.common.comment.CommentBottomPanelController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.CommentsViewModel
import kotlinx.coroutines.CoroutineScope

class TitleCommentAdapter(
    lifecycleScope: CoroutineScope,
    viewModel: CommentsViewModel,
    private val commentContentFactory: CommentBodyController.CommentContent.Factory,
) : BaseCommentAdapter<CommentViewHolder>(lifecycleScope, viewModel) {

    companion object : Analytics(LogAnalytic())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.Factory().build(parent.context, parent)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val model = getItem(position)
        if (model != null) return onBindViewHolder(holder, model)

        val event = analyticEvent("CommentAdapter", "Comment is null at position $position")
        return capture(event)
    }

    private fun onBindViewHolder(holder: CommentViewHolder, model: CommentModelElement) {
        CommentViewController(holder).default(model.comment).setLevel(model.level).setCommentAvatar(holder, model)
        CommentBodyController(holder).setContent(commentContentFactory.build(model.comment.message)).collapse()
        CommentBottomPanelController(holder).hide()
    }
}
