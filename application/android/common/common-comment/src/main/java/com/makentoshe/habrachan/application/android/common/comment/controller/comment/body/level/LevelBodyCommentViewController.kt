package com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.level

import android.view.LayoutInflater
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.R

class LevelBodyCommentViewController internal constructor(private val holder: CommentViewHolder) {

    fun setLevel(level: Int) {
        val inflater = LayoutInflater.from(holder.context)
        (0 until level).forEach { _ ->
            inflater.inflate(R.layout.layout_comment_level, holder.levelView, true)
        }
    }

    fun dispose() = holder.levelView.removeAllViews()
}