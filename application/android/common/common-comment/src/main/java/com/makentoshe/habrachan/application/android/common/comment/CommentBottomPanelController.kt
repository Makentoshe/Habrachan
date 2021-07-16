package com.makentoshe.habrachan.application.android.common.comment

import android.view.View
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentBottomPanelController(private val holder: CommentViewHolder) {

    private fun View.setOnClickListener(
        lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
    ) = setOnClickListener { lifecycleScope.launch(dispatcher) { listener() } }

    fun setVoteUpAction(
        lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
    ) = holder.voteUpView.setOnClickListener(lifecycleScope, dispatcher, listener)

    fun setVoteDownAction(
        lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
    ) = holder.voteDownView.setOnClickListener(lifecycleScope, dispatcher, listener)

    fun setReplyAction(
        lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
    ) = holder.voteDownView.setOnClickListener(lifecycleScope, dispatcher, listener)

    fun setShareAction(
        lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
    ) = holder.voteDownView.setOnClickListener(lifecycleScope, dispatcher, listener)

    fun setBookmarkAction(
        lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
    ) = holder.voteDownView.setOnClickListener(lifecycleScope, dispatcher, listener)

    fun setOverflowAction(
        lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
    ) = holder.voteDownView.setOnClickListener(lifecycleScope, dispatcher, listener)

    fun showExpanded() : CommentBottomPanelController {
        showCollapsed()
        holder.replyView.visibility = View.VISIBLE
        holder.shareView.visibility = View.VISIBLE
        holder.bookmarkView.visibility = View.VISIBLE
        return this
    }

    fun showCollapsed() {
        holder.voteDownView.visibility = View.VISIBLE
        holder.voteUpView.visibility = View.VISIBLE
        holder.voteScoreView.visibility = View.VISIBLE
        holder.overflowView.visibility = View.VISIBLE
    }

    fun hide() {
        holder.voteDownView.visibility = View.GONE
        holder.voteUpView.visibility = View.GONE
        holder.voteScoreView.visibility = View.GONE
        holder.overflowView.visibility = View.GONE
        holder.replyView.visibility = View.GONE
        holder.shareView.visibility = View.GONE
        holder.bookmarkView.visibility = View.GONE
    }
}