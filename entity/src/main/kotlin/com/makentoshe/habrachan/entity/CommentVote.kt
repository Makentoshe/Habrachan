package com.makentoshe.habrachan.entity

sealed class CommentVote {
    object Up : CommentVote()
    object Down : CommentVote()
}