package com.makentoshe.habrachan.application.android.screen.comments.model

import android.view.LayoutInflater
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.screen.comments.view.BlockViewHolder

class BlockViewController(private val holder: BlockViewHolder) {

    init {
        holder.levelView.removeAllViews()
        holder.blockText.text = ""
        holder.itemView.setOnClickListener(null)
    }

    fun setLevel(level: Int) : BlockViewController {
        val inflater = LayoutInflater.from(holder.context)
        (0 until level).forEach { _ ->
            inflater.inflate(R.layout.layout_comment_level, holder.levelView, true)
        }
        return this
    }

    fun setBody(count: Int, parent: Int, click: (Int) -> Unit) {
        holder.blockText.text = "Show discussion ($count)"
        holder.itemView.setOnClickListener { click(parent) }
    }
}