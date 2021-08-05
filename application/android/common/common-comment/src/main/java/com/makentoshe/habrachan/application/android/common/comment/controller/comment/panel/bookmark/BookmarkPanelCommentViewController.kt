package com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.bookmark

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.setOnClickListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class BookmarkPanelCommentViewController internal constructor(private val holder: CommentViewHolder) {

    fun setBookmarkAction(
        lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
    ) = holder.bookmarkView.setOnClickListener(lifecycleScope, dispatcher, listener)
}