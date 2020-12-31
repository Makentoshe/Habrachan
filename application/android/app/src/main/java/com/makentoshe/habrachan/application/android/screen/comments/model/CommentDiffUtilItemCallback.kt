package com.makentoshe.habrachan.application.android.screen.comments.model

import androidx.recyclerview.widget.DiffUtil

class CommentDiffUtilItemCallback: DiffUtil.ItemCallback<CommentModel>() {
    override fun areItemsTheSame(oldItem: CommentModel, newItem: CommentModel): Boolean {
        return oldItem.comment.id == newItem.comment.id
    }

    override fun areContentsTheSame(oldItem: CommentModel, newItem: CommentModel): Boolean {
        return oldItem.comment == newItem.comment
    }
}