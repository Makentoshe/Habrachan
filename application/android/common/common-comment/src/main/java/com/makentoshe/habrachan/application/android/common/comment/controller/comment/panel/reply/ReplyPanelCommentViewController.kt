package com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.reply

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.setOnClickListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ReplyPanelCommentViewController internal constructor(private val holder: CommentViewHolder) {

    fun setReplyAction(
        lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
    ) = holder.replyView.setOnClickListener(lifecycleScope, dispatcher, listener)
}