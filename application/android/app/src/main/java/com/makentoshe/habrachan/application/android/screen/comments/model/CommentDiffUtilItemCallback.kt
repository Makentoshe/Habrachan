package com.makentoshe.habrachan.application.android.screen.comments.model

import androidx.recyclerview.widget.DiffUtil

class CommentDiffUtilItemCallback: DiffUtil.ItemCallback<CommentEntityModel>() {
    override fun areItemsTheSame(oldItem: CommentEntityModel, newItem: CommentEntityModel): Boolean {
        return oldItem.comment.id == newItem.comment.id
    }

    override fun areContentsTheSame(oldItem: CommentEntityModel, newItem: CommentEntityModel): Boolean {
        return oldItem.comment == newItem.comment
    }
}