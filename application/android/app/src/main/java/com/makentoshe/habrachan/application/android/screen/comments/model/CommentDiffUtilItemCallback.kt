package com.makentoshe.habrachan.application.android.screen.comments.model

import androidx.recyclerview.widget.DiffUtil

class CommentDiffUtilItemCallback : DiffUtil.ItemCallback<CommentAdapterModel>() {

    override fun areItemsTheSame(oldItem: CommentAdapterModel, newItem: CommentAdapterModel): Boolean {
        return if (oldItem is CommentAdapterModel.Comment && newItem is CommentAdapterModel.Comment) {
            oldItem.comment.id == newItem.comment.id
        } else if (oldItem is CommentAdapterModel.Block && newItem is CommentAdapterModel.Block) {
            oldItem == newItem
        } else false
    }

    override fun areContentsTheSame(oldItem: CommentAdapterModel, newItem: CommentAdapterModel): Boolean {
        return if (oldItem is CommentAdapterModel.Comment && newItem is CommentAdapterModel.Comment) {
            oldItem == newItem
        } else if (oldItem is CommentAdapterModel.Block && newItem is CommentAdapterModel.Block) {
            oldItem == newItem
        } else false
    }
}