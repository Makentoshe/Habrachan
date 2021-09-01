package com.makentoshe.habrachan.entity

interface CommentMessage {
    val message: String
}

fun CommentMessage.asString(): String {
    return "CommentMessage(message=$message)"
}

fun commentMessage(message: String) = object : CommentMessage {
    override val message: String = message
}