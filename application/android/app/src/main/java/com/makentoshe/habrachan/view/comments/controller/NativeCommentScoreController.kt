package com.makentoshe.habrachan.view.comments.controller

import android.widget.TextView
import com.makentoshe.habrachan.common.ui.TextScoreController

class NativeCommentScoreController(private val textView: TextView) {

    fun setCommentScore(commentScore: Int) {
        TextScoreController(textView).setScoreDark(textView.context, commentScore)
    }

    class Factory {
        fun build(textView: TextView) = NativeCommentScoreController(textView)
    }
}
