package com.makentoshe.habrachan.application.android.screen.comments.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R

class BlockViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val context: Context = view.context
    val blockText: TextView = itemView.findViewById(R.id.layout_comment_block_text)
    val levelView: ViewGroup = view.findViewById(R.id.layout_comment_block_level)
}