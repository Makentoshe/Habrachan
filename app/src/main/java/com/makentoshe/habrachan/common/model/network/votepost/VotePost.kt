package com.makentoshe.habrachan.common.model.network.votepost

import com.makentoshe.habrachan.common.model.network.CookieStorage
import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result

class VotePost(
    private val api: HabrApi,
    private val cookieStorage: CookieStorage,
    private val factory: VotePostConverterFactory
) {

    fun execute(request: VotePostRequest): Result.VotePostResponse {
        val response = api.votePostThroughApi(
            clientKey = request.clientKey,
            accessToken = request.accessToken,
            postId = request.postId,
            score = request.vote.toScore()
        ).execute()
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            factory.converter.convert(response.errorBody()!!)
        }
    }
}