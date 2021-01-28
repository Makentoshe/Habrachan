package com.makentoshe.habrachan.application.android.screen.comments.model

import android.view.View
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.screen.comments.view.CommentsEmptyStateViewHolder

class CommentsEmptyStateController(private val holder: CommentsEmptyStateViewHolder) {

    fun show() {
        holder.root.visibility = View.VISIBLE
        holder.title.setText(R.string.layout_comments_empty_state_title)
        holder.message.setText(R.string.layout_comments_empty_state_message)
        holder.button.setText(R.string.layout_comments_empty_state_button)
    }

    fun hide() {
        holder.root.visibility = View.GONE
    }

    fun buttonOnClickListener(listener: (View) -> Unit) {
        holder.button.setOnClickListener(listener)
    }

}