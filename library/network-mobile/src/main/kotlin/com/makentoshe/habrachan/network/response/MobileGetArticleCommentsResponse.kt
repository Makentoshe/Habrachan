package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.mobiles.CommentAccess
import com.makentoshe.habrachan.entity.mobiles.Moderated
import com.makentoshe.habrachan.entity.natives.Comment
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import com.makentoshe.habrachan.network.request.MobileGetArticleCommentsRequest

class MobileGetArticleCommentsResponse(
    override val request: GetArticleCommentsRequest,
    override val data: List<Comment>,
    val commentAccess: CommentAccess,
    val lastCommentTimestamp: Int,
    val moderated: Moderated,
    val threads: List<Int>
) : GetArticleCommentsResponse {

    class Factory(
        @SerializedName("commentAccess")
        val commentAccess: CommentAccess,
        @SerializedName("comments")
        val comments: List<Comment>,
        @SerializedName("lastCommentTimestamp")
        val lastCommentTimestamp: Int, // 1552555368
        @SerializedName("moderated")
        val moderated: Moderated,
        @SerializedName("threads")
        val threads: List<Int>
    ) {
        fun build(request: MobileGetArticleCommentsRequest): MobileGetArticleCommentsResponse {
            return MobileGetArticleCommentsResponse(request, comments, commentAccess, lastCommentTimestamp, moderated, threads)
        }
    }
}