package com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.vote.down

import androidx.core.content.ContextCompat
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.R
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.setOnClickListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class DownVotePanelCommentViewControllerController internal constructor(private val holder: CommentViewHolder) {

    fun setVoteDownAction(
        lifecycleScope: CoroutineScope, dispatcher: CoroutineDispatcher = Dispatchers.IO, listener: suspend () -> Unit
    ) = holder.voteDownView.setOnClickListener(lifecycleScope, dispatcher, listener)

    fun setVoteDownIcon() = holder.voteDownView.apply {
        setColorFilter(ContextCompat.getColor(holder.context, R.color.negative))
    }.setImageResource(R.drawable.ic_arrow_bold_solid)

    fun resetVoteDownIcon() = holder.voteDownView.apply {
        setColorFilter(ContextCompat.getColor(holder.context, R.color.text))
    }.setImageResource(R.drawable.ic_arrow_bold_outline)
}