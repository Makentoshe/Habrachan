package com.makentoshe.habrachan.network.response

import com.makentoshe.habrachan.entity.natives.Comment
import com.makentoshe.habrachan.network.request.GetCommentsRequest2

interface GetCommentsResponse {
    val request: GetCommentsRequest2
    val data: List<Comment>
}