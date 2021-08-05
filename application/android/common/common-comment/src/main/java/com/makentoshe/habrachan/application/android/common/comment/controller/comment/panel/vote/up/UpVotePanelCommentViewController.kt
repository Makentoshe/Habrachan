package com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.vote.up

import androidx.core.content.ContextCompat
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.R
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.setOnClickListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class UpVotePanelCommentViewController internal constructor(private val holder: CommentViewHolder) {

    fun setVoteUpAction(
        lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
    ) = holder.voteUpView.setOnClickListener(lifecycleScope, dispatcher, listener)

    fun setVoteUpIcon() = holder.voteUpView.apply {
        setColorFilter(ContextCompat.getColor(holder.context, R.color.positive))
    }.setImageResource(R.drawable.ic_arrow_bold_solid)

    fun resetVoteUpIcon() = holder.voteUpView.apply {
        setColorFilter(ContextCompat.getColor(holder.context, R.color.text))
    }.setImageResource(R.drawable.ic_arrow_bold_outline)
}