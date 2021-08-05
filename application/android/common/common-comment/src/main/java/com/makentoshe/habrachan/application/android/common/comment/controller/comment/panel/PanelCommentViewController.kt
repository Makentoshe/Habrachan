package com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel

import android.view.View
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.bookmark.BookmarkPanelCommentViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.overflow.OverflowPanelCommentViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.reply.ReplyPanelCommentViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.share.SharePanelCommentViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.vote.VotePanelCommentViewController
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PanelCommentViewController internal constructor(private val holder: CommentViewHolder) {

    val vote by lazy { VotePanelCommentViewController(holder) }

    val bookmark by lazy { BookmarkPanelCommentViewController(holder) }

    val overflow by lazy { OverflowPanelCommentViewController(holder) }

    val reply by lazy { ReplyPanelCommentViewController(holder) }

    val share by lazy { SharePanelCommentViewController(holder) }

    fun dispose() {
        vote.dispose()
    }

    fun showExpanded() {
        showCollapsed()
        holder.replyView.visibility = View.VISIBLE
        holder.shareView.visibility = View.VISIBLE
        holder.bookmarkView.visibility = View.VISIBLE
    }

    fun showCollapsed() {
        holder.voteDownView.visibility = View.VISIBLE
        holder.voteUpView.visibility = View.VISIBLE
        holder.voteScoreView.visibility = View.VISIBLE
        holder.overflowView.visibility = View.VISIBLE
    }

    fun showHidden() {
        holder.voteDownView.visibility = View.GONE
        holder.voteUpView.visibility = View.GONE
        holder.voteScoreView.visibility = View.GONE
        holder.overflowView.visibility = View.GONE
        holder.replyView.visibility = View.GONE
        holder.shareView.visibility = View.GONE
        holder.bookmarkView.visibility = View.GONE
    }

}

internal fun View.setOnClickListener(
    lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
) = setOnClickListener { lifecycleScope.launch(dispatcher) { listener() } }

