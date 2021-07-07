package com.makentoshe.habrachan.application.android.common.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BlockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val context: Context = view.context
    val blockText: TextView = itemView.findViewById(R.id.layout_comment_block_text)
    val levelView: ViewGroup = view.findViewById(R.id.layout_comment_block_level)

    class Factory {

        fun create(context: Context, parent: ViewGroup? = null): BlockViewHolder {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.layout_comment_block, parent, false)
            return BlockViewHolder(view)
        }
    }
}