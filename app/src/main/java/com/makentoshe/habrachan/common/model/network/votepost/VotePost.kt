package com.makentoshe.habrachan.common.model.network.votepost

import com.makentoshe.habrachan.common.model.network.CookieStorage
import com.makentoshe.habrachan.common.model.network.HabrApi

class VotePost(
    private val api: HabrApi,
    private val cookieStorage: CookieStorage
) {

    fun execute(request: VotePostRequest): VotePostResult {
        return executeThroughApi(request) ?: errorResult()
    }

    private fun executeThroughApi(request: VotePostRequest): VotePostResult? {
        val response = api.votePostThroughApi(
            clientKey = request.clientKey,
            accessToken = request.accessToken,
            postId = request.postId,
            score = request.vote.toScore()
        ).execute()
        return response.body()
    }

    private fun errorResult(): VotePostResult {
        return VotePostResult(success = false)
    }
}