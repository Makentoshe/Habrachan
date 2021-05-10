package com.makentoshe.habrachan.network.response

import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest

interface GetArticleCommentsResponse {
    val request: GetArticleCommentsRequest
    val data: List<Comment>
}