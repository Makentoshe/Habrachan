package com.makentoshe.habrachan.application.android.common.comment

import android.view.View

class CommentBottomPanelController(private val holder: CommentViewHolder) {

    fun setVoteUpAction(listener: () -> Unit) {
        holder.voteUpView.setOnClickListener { listener() }
    }

    fun setVoteDownAction(listener: () -> Unit) {
        holder.voteDownView.setOnClickListener { listener() }
    }

    fun setReplyAction(listener: () -> Unit) {
        holder.replyView.setOnClickListener { listener() }
    }

    fun setShareAction(listener: () -> Unit) {
        holder.shareView.setOnClickListener { listener() }
    }

    fun setBookmarkAction(listener: () -> Unit) {
        holder.bookmarkView.setOnClickListener { listener() }
    }

    fun setOverflowAction(listener: () -> Unit) {
        holder.overflowView.setOnClickListener { listener() }
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