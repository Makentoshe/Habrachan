package com.makentoshe.habrachan.application.android.screen.comments.model

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import com.makentoshe.habrachan.R

class CommentParentAdapter(
    private val commentModel: CommentModel
): PagingDataAdapter<CommentModel, CommentViewHolder>(CommentDiffUtilItemCallback()) {

    override fun getItemCount() = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CommentViewHolder(inflater.inflate(R.layout.layout_comment_item, parent, false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val controller = CommentViewController(holder)
        controller.render(commentModel.comment)
        controller.setReplies(0) {
            // TODO implement comments replying
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        }
        // TODO implement comments voting
        controller.setVoteListener({
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        }, {
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        })
    }

}