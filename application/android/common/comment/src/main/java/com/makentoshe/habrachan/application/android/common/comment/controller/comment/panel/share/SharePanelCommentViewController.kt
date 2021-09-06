package com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.share

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.setOnClickListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SharePanelCommentViewController internal constructor(private val holder: CommentViewHolder) {

    fun setShareAction(
        lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
    ) = holder.shareView.setOnClickListener(lifecycleScope, dispatcher, listener)
}