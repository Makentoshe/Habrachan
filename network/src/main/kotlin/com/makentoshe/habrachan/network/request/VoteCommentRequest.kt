package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.CommentVote

@Deprecated("Use interface instead", level = DeprecationLevel.WARNING)
data class VoteCommentRequest(val client: String, val token: String, val commentId: Int)

interface VoteCommentRequest2 : Request {
    val commentId: Int
    val commentVote: CommentVote
}
