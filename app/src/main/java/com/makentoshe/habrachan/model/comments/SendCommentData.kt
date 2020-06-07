package com.makentoshe.habrachan.model.comments

import android.text.Editable

data class SendCommentData(val message: Editable, val replyToParentId: Int = 0) {
    init {
        if (replyToParentId < 0) throw IllegalArgumentException("${this::replyToParentId.name} should not be less 0")
    }
}