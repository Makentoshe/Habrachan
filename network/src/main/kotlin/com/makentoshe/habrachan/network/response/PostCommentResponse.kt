package com.makentoshe.habrachan.network.response

import com.makentoshe.habrachan.entity.PostedComment
import com.makentoshe.habrachan.network.request.PostCommentRequest

interface PostCommentResponse {
    val request: PostCommentRequest
    val comment: PostedComment
}

fun postCommentResponse(request: PostCommentRequest, comment: PostedComment) = object : PostCommentResponse {
    override val request: PostCommentRequest = request
    override val comment: PostedComment = comment
}