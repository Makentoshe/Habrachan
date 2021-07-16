package com.makentoshe.habrachan.entity

import java.text.SimpleDateFormat
import java.util.*

interface Comment : CommentId {
    val author: CommentAuthor
    val level: Int
    val message: String
    val parentId: Int
    val score: Int
    val avatar: String?

    val isAuthor: Boolean
    val isCanVote: Boolean
    val isFavorite: Boolean

    val timePublishedRaw: String
    val timeChangedRaw: String?

    val voteRaw: Int?
}

val Comment.timePublished: Date
    get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(timePublishedRaw)

val Comment.timeChanged: Date?
    get() = if (timeChangedRaw != null) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(timeChangedRaw) else null

val Comment.vote: CommentVote?
    get() = when (voteRaw) {
        1 -> CommentVote.Up
        -1 -> CommentVote.Down
        else -> null
    }

fun comment(
    id: Int,
    level: Int,
    commentAuthor: CommentAuthor,
    message: String,
    parentId: Int,
    score: Int,
    avatar: String?,
    isAuthor: Boolean,
    isCanVote: Boolean,
    isFavorite: Boolean,
    timePublishedRaw: String,
    timeChangedRaw: String?,
    voteRaw: Int?
) = object : Comment {
    override val commentId = id
    override val author = commentAuthor
    override val level = level
    override val message = message
    override val parentId = parentId
    override val score = score
    override val avatar = avatar
    override val isAuthor = isAuthor
    override val isCanVote = isCanVote
    override val isFavorite = isFavorite
    override val timeChangedRaw = timeChangedRaw
    override val timePublishedRaw = timePublishedRaw
    override val voteRaw: Int? = voteRaw
}
