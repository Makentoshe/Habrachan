package com.makentoshe.habrachan.common.model.network

import com.makentoshe.habrachan.common.model.entity.Data
import com.makentoshe.habrachan.common.model.entity.GetPostsBySearchResult

class GetPostsBySearch(
    private val api: HabrApi
) {
    fun execute(request: GetPostsBySearchRequest): GetPostsBySearchResult {
        if (request.page < 1) return createErrorResult(400, "Page should not be less 0")

        val response = api.getPostsBySearch(
            query = request.query, page = request.page, hl = request.hl, fl = request.fl
        ).execute()

        return response.body() ?: createErrorResult(response.code(), response.message())
    }

    private fun createErrorResult(code: Int, message: String): GetPostsBySearchResult {
        val data = createErrorData(code, message)
        return GetPostsBySearchResult(data, false)
    }

    private fun createErrorData(code: Int, message: String?) = Data(
        code = code, message = message, articles = null, pages = null, data = null
    )
}
