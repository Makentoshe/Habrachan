package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.network.request.VoteCommentRequest2

@Deprecated("User interface instead")
data class VoteCommentResponse(
    @SerializedName("score")
    val score: Int,
    @SerializedName("server_time")
    val serverTime: String
)

interface VoteCommentResponse2 {
    val request: VoteCommentRequest2

    /** The updated score for comment */
    val score: Int
}