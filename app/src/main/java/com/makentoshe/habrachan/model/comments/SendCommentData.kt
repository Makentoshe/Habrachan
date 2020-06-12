package com.makentoshe.habrachan.model.comments

import android.text.Editable

data class SendCommentData(val message: Editable, val articleId: Int, val replyToParentId: Int = 0)
