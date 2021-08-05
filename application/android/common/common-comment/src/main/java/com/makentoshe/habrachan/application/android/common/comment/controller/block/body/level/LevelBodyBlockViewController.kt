package com.makentoshe.habrachan.application.android.common.comment.controller.block.body.level

import android.view.LayoutInflater
import com.makentoshe.habrachan.application.android.common.comment.BlockViewHolder
import com.makentoshe.habrachan.application.android.common.comment.R

class LevelBodyBlockViewController internal constructor(private val holder: BlockViewHolder) {

    fun setLevel(level: Int) {
        val inflater = LayoutInflater.from(holder.context)
        (0 until level).forEach { _ ->
            inflater.inflate(R.layout.layout_comment_level, holder.levelView, true)
        }
    }

    fun dispose() = holder.levelView.removeAllViews()
}