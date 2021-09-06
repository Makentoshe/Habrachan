package com.makentoshe.habrachan.application.android.common.comment.controller.comment.body

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.author.AuthorBodyCommentViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.avatar.AvatarBodyCommentViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content.ContentBodyCommentViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.level.LevelBodyCommentViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.timestamp.TimestampBodyCommentViewController
import com.makentoshe.habrachan.application.android.common.dp2px

class BodyCommentViewController internal constructor(private val holder: CommentViewHolder) {

    private val maxCollapsedHeight = holder.context.dp2px(100f).toInt()
    private val maxExpandedHeight = Int.MAX_VALUE

    val avatar by lazy { AvatarBodyCommentViewController(holder) }

    val timestamp by lazy { TimestampBodyCommentViewController(holder) }

    val author by lazy { AuthorBodyCommentViewController(holder) }

    val level by lazy { LevelBodyCommentViewController(holder) }

    val content by lazy { ContentBodyCommentViewController(holder) }

    fun collapse() = with(holder.bodyView) {
        maxHeight = maxCollapsedHeight
        isEnabled = false
    }

    fun expand() = with(holder.bodyView) {
        maxHeight = maxExpandedHeight
        isEnabled = true
    }

    fun dispose() {
        holder.levelView.removeAllViews()
        holder.voteScoreView.text = ""
        author.dispose()
        avatar.dispose()
        timestamp.dispose()
        content.dispose()
    }
}
