package com.makentoshe.habrachan.application.android.screen.comments.articles.model

import android.view.View
import com.makentoshe.habrachan.application.android.screen.comments.articles.R
import com.makentoshe.habrachan.application.android.screen.comments.articles.view.CommentsEmptyStateViewHolder

class CommentsEmptyStateController(private val holder: CommentsEmptyStateViewHolder) {

    private val emptyStateImageSet = setOf(
        R.drawable.drawable_empty_state_abstract_1,
        R.drawable.drawable_empty_state_abstract_2,
        R.drawable.drawable_empty_state_abstract_3
    )

    fun show() {
        holder.root.visibility = View.VISIBLE
        holder.title.setText(R.string.layout_comments_empty_state_title)
        holder.message.setText(R.string.layout_comments_empty_state_message)
        holder.button.setText(R.string.layout_comments_empty_state_button)

        if (holder.image.drawable == null) holder.image.setImageResource(emptyStateImageSet.random())
    }

    fun hide() {
        holder.root.visibility = View.GONE
    }

    fun buttonOnClickListener(listener: (View) -> Unit) {
        holder.button.setOnClickListener(listener)
    }

}