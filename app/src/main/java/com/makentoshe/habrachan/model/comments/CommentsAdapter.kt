package com.makentoshe.habrachan.model.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.view.comments.controller.CommentController

class CommentsAdapter(
    private val comments: List<Comment>,
    private val commentController: CommentController
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.authorView.text = comments[position].author.login
        holder.timePublishedView.text = comments[position].timePublished
        commentController.messageFactory().build(holder.messageView).setCommentText(comments[position].message)
        commentController.scoreFactory().build(holder.scoreView).setCommentScore(comments[position].score)
        commentController.avatarFactory().build(holder.avatarView).setCommentAvatar(comments[position].avatar) {
            holder.avatarView.visibility = View.VISIBLE
            holder.progressView.visibility = View.GONE
        }
    }

    override fun getItemCount() = comments.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageView = itemView.findViewById<TextView>(R.id.comments_fragment_comment_message)
        val authorView = itemView.findViewById<TextView>(R.id.comments_fragment_comment_author)
        val timePublishedView = itemView.findViewById<TextView>(R.id.comments_fragment_comment_timepublished)
        val scoreView = itemView.findViewById<TextView>(R.id.comments_fragment_comment_score)
        val avatarView = itemView.findViewById<ImageView>(R.id.comments_fragment_comment_avatar)
        val progressView = itemView.findViewById<ProgressBar>(R.id.comments_fragment_comment_progressbar)
    }

    class Factory(private val commentController: CommentController) {
        fun build(comments: List<Comment>): CommentsAdapter {
            return CommentsAdapter(comments, commentController)
        }
    }
}