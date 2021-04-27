package com.makentoshe.habrachan.application.android.screen.comments.model

import androidx.recyclerview.widget.DiffUtil

class CommentDiffUtilItemCallback : DiffUtil.ItemCallback<CommentModelElement>() {

    override fun areItemsTheSame(oldItem: CommentModelElement, newItem: CommentModelElement): Boolean {
        return oldItem.comment.commentId == newItem.comment.commentId
    }

    override fun areContentsTheSame(oldItem: CommentModelElement, newItem: CommentModelElement): Boolean {
        return oldItem.equals(newItem)
    }
}