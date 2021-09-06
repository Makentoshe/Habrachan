package com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content

import android.view.View
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.entity.Comment

class ContentBodyCommentViewController internal constructor(private val holder: CommentViewHolder) {

    init {
        holder.bodyView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            holder.fadeView.visibility = if (holder.bodyView.height == holder.bodyView.maxHeight) View.VISIBLE else View.GONE
        }
    }

    fun setContent(content: ContentBodyComment) {
        content.setContentToView(holder.bodyView)
    }

    fun setContent(comment: Comment) = setContent(ContentBodyComment.Factory(holder.context).build(comment.message))

    fun dispose() {

    }
}