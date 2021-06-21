package com.makentoshe.habrachan.network.deserialize

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.request.VoteArticleRequest
import com.makentoshe.habrachan.network.response.VoteArticleResponse

interface VoteArticleDeserializer<Request: VoteArticleRequest> {

    fun success(request: Request, json: String): Result<VoteArticleResponse>

    fun failure(request: Request, json: String): Result<VoteArticleResponse>
}