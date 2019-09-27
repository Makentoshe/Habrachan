package com.makentoshe.habrachan.common.model.network.users

import com.makentoshe.habrachan.common.model.entity.Data
import com.makentoshe.habrachan.common.model.network.HabrApi

class GetUsersBySearch(private val api: HabrApi) {

    fun execute(request: GetUsersBySearchRequest): GetUsersBySearchResult {
        if (request.page < 1) return createErrorResult(400, "Page should not be less 1")
        return executeThroughMobileApi(request) ?: createErrorResult(404, "No one of the presented apis are not available")
    }

    private fun executeThroughMobileApi(request: GetUsersBySearchRequest): GetUsersBySearchResult? {
        return api.getUsersBySearch(page = request.page, query = request.query).execute().body()
    }

    private fun createErrorResult(code: Int, message: String): GetUsersBySearchResult {
        val data = createErrorData(code, message)
        return GetUsersBySearchResult(data = data, success = false)
    }

    private fun createErrorData(code: Int, message: String?) = Data(
        code = code, message = message, articles = null, pages = null, data = null, users = null
    )
}