package com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.timestamp

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.time
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.entity.timePublished

class TimestampBodyCommentViewController internal constructor(private val holder: CommentViewHolder) {

    fun setTimestamp(timestamp: String) = holder.timestampView.setText(timestamp)

    fun setTimestamp(comment: Comment) = setTimestamp(comment.timePublished.time(holder.context,
        com.makentoshe.habrachan.application.android.common.comment.R.string.format_comment_time
    ))

    fun dispose() = holder.timestampView.setText("")
}