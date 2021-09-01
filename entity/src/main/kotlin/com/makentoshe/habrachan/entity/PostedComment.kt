package com.makentoshe.habrachan.entity

/** Comment that was posted */
interface PostedComment : CommentId, CommentMessage, TimePublished {
    val onModerated: Boolean
}

/** Represents a [PostedComment] as a human-readable string */
fun PostedComment.asString() : String {
    return "PostedComment(commentId=$commentId, message=$message, timePublished=$rawTimePublished, onModerated=$onModerated)"
}

fun postedComment(
    commentId: CommentId,
    commentMessage: CommentMessage,
    timePublished: TimePublished,
    onModerated: Boolean
) = postedComment(commentId.commentId, commentMessage.message, timePublished.rawTimePublished, onModerated)

fun postedComment(
    commentId: Int,
    commentMessage: String,
    rawTimePublished: String,
    onModerated: Boolean,
) = object : PostedComment {
    override val commentId: Int = commentId
    override val message: String = commentMessage
    override val rawTimePublished: String = rawTimePublished
    override val onModerated: Boolean = onModerated
}