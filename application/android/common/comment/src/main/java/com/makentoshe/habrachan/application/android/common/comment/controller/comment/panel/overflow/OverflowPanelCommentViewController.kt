package com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.overflow

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.setOnClickListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class OverflowPanelCommentViewController internal constructor(private val holder: CommentViewHolder) {

    fun setOverflowAction(
        lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
    ) = holder.overflowView.setOnClickListener(lifecycleScope, dispatcher, listener)
}