package com.makentoshe.habrachan.network.response

import com.makentoshe.habrachan.network.request.PostCommentRequest

interface PostCommentResponse {
    val request: PostCommentRequest
}

fun postCommentResponse(request: PostCommentRequest) = object: PostCommentResponse {
    override val request: PostCommentRequest = request
}