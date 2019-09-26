package com.makentoshe.habrachan.common.model.network.votepost

import com.makentoshe.habrachan.common.model.entity.Vote

data class VotePostRequest(
    val postId: Int,
    val vote: Vote,
    val clientKey: String,
    val accessToken: String
)
