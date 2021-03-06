package com.makentoshe.habrachan.application.android.common.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val context: Context = view.context

    val avatarView: ImageView = view.findViewById(R.id.layout_comment_item_avatar)
    val authorView: TextView = view.findViewById(R.id.layout_comment_item_author)
    val timestampView: TextView = view.findViewById(R.id.layout_comment_item_timestamp)
    val bodyView: TextView = view.findViewById(R.id.layout_comment_item_body)

    val voteUpView: ImageView = view.findViewById(R.id.layout_comment_item_vote_up)
    val voteScoreView: TextView = view.findViewById(R.id.layout_comment_item_vote_text)
    val voteDownView: ImageView = view.findViewById(R.id.layout_comment_item_vote_down)
    val levelView: ViewGroup = view.findViewById(R.id.layout_comment_item_level)
    val replyView: View = view.findViewById(R.id.layout_comment_item_reply)
    val shareView: View = view.findViewById(R.id.layout_comment_item_share)
    val bookmarkView: View = view.findViewById(R.id.layout_comment_item_bookmark)
    val overflowView: View = view.findViewById(R.id.layout_comment_item_overflow)

    val fadeView: View = view.findViewById(R.id.layout_comment_item_fade)
    val expandView: TextView = view.findViewById(R.id.layout_comment_item_expand)

    fun setOnClickListenerForHeader(listener: View.OnClickListener) {
        avatarView.setOnClickListener(listener)
        authorView.setOnClickListener(listener)
        timestampView.setOnClickListener(listener)
    }

    class Factory {

        fun build(context: Context, parent: ViewGroup? = null): CommentViewHolder {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.layout_comment_item, parent, false)
            return CommentViewHolder(view)
        }

        fun build(parent: ViewGroup): CommentViewHolder = build(parent.context, parent)
    }
}

