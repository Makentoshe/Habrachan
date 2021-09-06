package com.makentoshe.habrachan.application.android.common.comment.controller.comment

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.BodyCommentViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.panel.PanelCommentViewController

@Suppress("UsePropertyAccessSyntax")// ignore setText function for inlining format
class CommentViewController(private val holder: CommentViewHolder) {

    val body by lazy { BodyCommentViewController(holder) }

    val panel by lazy { PanelCommentViewController(holder) }

    fun dispose() {
        body.dispose()
        panel.dispose()
    }
}
