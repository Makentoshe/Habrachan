package com.makentoshe.habrachan.network.deserializer

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.deserialize.VoteArticleDeserializer
import com.makentoshe.habrachan.network.exception.NativeVoteArticleDeserializerException
import com.makentoshe.habrachan.network.request.NativeVoteArticleRequest
import com.makentoshe.habrachan.network.response.NativeVoteArticleResponse

open class NativeVoteArticleDeserializer : GsonDeserializer(), VoteArticleDeserializer<NativeVoteArticleRequest> {

    override fun success(request: NativeVoteArticleRequest, json: String): Result<NativeVoteArticleResponse> = try {
        Result.success(gson.fromJson(json, NativeVoteArticleResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(NativeVoteArticleDeserializerException(request, json, exception))
    }

    override fun failure(request: NativeVoteArticleRequest, json: String): Result<NativeVoteArticleResponse> {
        return Result.failure(NativeVoteArticleDeserializerException(request, json))
    }
}