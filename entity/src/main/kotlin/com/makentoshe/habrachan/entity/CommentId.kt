package com.makentoshe.habrachan.entity

interface CommentId {
    val commentId: Int
}

fun commentId(id: Int) = object: CommentId {
    override val commentId: Int = id
}