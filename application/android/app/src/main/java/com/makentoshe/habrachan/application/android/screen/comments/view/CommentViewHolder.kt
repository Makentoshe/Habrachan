package com.makentoshe.habrachan.application.android.screen.comments.view

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R

// TODO move holder to controller class
class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val context: Context = view.context
    val replyTextView: TextView = view.findViewById(R.id.layout_comment_item_replies)
    val avatarView: ImageView = view.findViewById(R.id.layout_comment_item_avatar)
    val authorView: TextView = view.findViewById(R.id.layout_comment_item_author)
    val timestampView: TextView = view.findViewById(R.id.layout_comment_item_timestamp)
    val bodyView: TextView = view.findViewById(R.id.layout_comment_item_body)
    val voteView: View = view.findViewById(R.id.layout_comment_item_vote)
    val voteUpView: View = view.findViewById(R.id.layout_comment_item_vote_up)
    val voteScoreView: TextView = view.findViewById(R.id.layout_comment_item_vote_score)
    val voteDownView: View = view.findViewById(R.id.layout_comment_item_vote_down)
}