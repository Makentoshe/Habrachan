package com.makentoshe.habrachan.entity

interface Comment: CommentId {
    val author: CommentAuthor
    val level: Int
    val message: String
    val parentId: Int
    val score: Int

    val isAuthor: Boolean
    val isCanVote: Boolean
    val isFavorite: Boolean

    val timeChangedRaw: String?
    val timePublishedRaw: String
}

