package com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.author

import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.entity.Comment

class AuthorBodyCommentViewController internal constructor(private val holder: CommentViewHolder) {

    fun setAuthor(author: String) = holder.authorView.setText(author)

    fun setAuthor(comment: Comment) = setAuthor(comment.author.login)

    fun dispose() = holder.authorView.setText("")
}