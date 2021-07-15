package com.makentoshe.habrachan.network.request

@Deprecated("Use interface instead", level = DeprecationLevel.WARNING)
data class VoteCommentRequest(val client: String, val token: String, val commentId: Int)

interface VoteCommentRequest2 : Request {
    val commentId: Int
    val commentVote: CommentVote
}

sealed class CommentVote {
    object Up : CommentVote()
    object Down : CommentVote()
}
