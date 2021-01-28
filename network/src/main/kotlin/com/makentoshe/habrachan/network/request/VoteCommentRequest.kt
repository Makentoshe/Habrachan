package com.makentoshe.habrachan.network.request

data class VoteCommentRequest(val client: String, val token: String, val commentId: Int)
