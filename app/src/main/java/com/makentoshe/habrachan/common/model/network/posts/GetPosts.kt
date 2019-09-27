package com.makentoshe.habrachan.common.model.network.posts

import com.makentoshe.habrachan.common.model.entity.Data
import com.makentoshe.habrachan.common.model.network.HabrApi

class GetPosts(
    private val api: HabrApi
) {
    fun execute(request: GetPostsRequest): GetPostsResult {
        if (request.page < 1) return createErrorResult(400, "Page should not be less 0")

        val response = api.getPosts(
            page = request.page,
            sort = request.sort,
            hl = request.hl,
            fl = request.fl,
            date = request.date,
            custom = request.custom
        ).execute()

        return response.body() ?: createErrorResult(response.code(), response.message())
    }

    private fun createErrorResult(code: Int, message: String): GetPostsResult {
        val data = createErrorData(code, message)
        return GetPostsResult(data, false)
    }

    private fun createErrorData(code: Int, message: String?) = Data(
        code = code, message = message, articles = null, pages = null, data = null, users = null
    )
}