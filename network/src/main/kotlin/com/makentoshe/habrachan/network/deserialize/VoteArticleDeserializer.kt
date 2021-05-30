package com.makentoshe.habrachan.network.deserialize

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.request.VoteArticleRequest
import com.makentoshe.habrachan.network.response.VoteArticleResponse

interface VoteArticleDeserializer {

    fun success(request: VoteArticleRequest, json: String): Result<VoteArticleResponse>

    fun failure(request: VoteArticleRequest, json: String): Result<VoteArticleResponse>
}