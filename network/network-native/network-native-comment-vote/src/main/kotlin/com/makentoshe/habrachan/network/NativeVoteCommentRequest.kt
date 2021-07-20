package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.entity.CommentVote
import com.makentoshe.habrachan.network.request.NativeRequest
import com.makentoshe.habrachan.network.request.VoteCommentRequest2

class NativeVoteCommentRequest(
    val userSession: UserSession, override val commentId: CommentId, override val commentVote: CommentVote
) : NativeRequest(), VoteCommentRequest2