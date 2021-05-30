package com.makentoshe.habrachan.network.response

import com.makentoshe.habrachan.network.request.VoteArticleRequest

interface VoteArticleResponse {
    val request: VoteArticleRequest
    /** The updated score for article */
    val score: Int
}
