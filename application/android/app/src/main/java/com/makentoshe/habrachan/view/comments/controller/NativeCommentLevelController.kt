package com.makentoshe.habrachan.view.comments.controller

import android.view.LayoutInflater
import android.view.ViewGroup
import com.makentoshe.habrachan.R
import kotlin.math.min

class NativeCommentLevelController(private val verticalContainer: ViewGroup) {

    fun setCommentLevel(level: Int) {
        verticalContainer.removeAllViews()
        val layoutInflater = LayoutInflater.from(verticalContainer.context)
        repeat(min(level, 10)) {
            layoutInflater.inflate(R.layout.comment_item_level, verticalContainer, true)
        }
    }

    class Factory {
        fun build(verticalContainer: ViewGroup) = NativeCommentLevelController(verticalContainer)
    }
}