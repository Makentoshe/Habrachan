package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.CommentVote
import com.makentoshe.habrachan.network.request.VoteCommentRequest2
import com.makentoshe.habrachan.network.response.VoteCommentResponse2

interface VoteCommentManager<Request: VoteCommentRequest2> {

    /**
     * Factory method creates request
     *
     * @param commentId
     * @return a request for performing network operation
     * */
    fun request(userSession: UserSession, commentId: Int, vote: CommentVote): Request

    /** Main network method returns vote comment response by [request] */
    suspend fun vote(request: Request): Result<VoteCommentResponse2>
}