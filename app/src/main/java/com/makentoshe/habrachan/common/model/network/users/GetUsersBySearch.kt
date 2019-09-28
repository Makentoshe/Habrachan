package com.makentoshe.habrachan.common.model.network.users

import com.makentoshe.habrachan.common.model.entity.UsersData
import com.makentoshe.habrachan.common.model.network.HabrApi

class GetUsersBySearch(private val api: HabrApi) {

    fun execute(request: GetUsersBySearchRequest): GetUsersBySearchResult {
        if (request.page < 1) return createErrorResult(400, "Page should not be less than 1")
        val response = api.getUsersBySearch(page = request.page, query = request.query).execute()
        return response.body() ?: createErrorResult(
            response.code(), response.errorBody()?.string() ?: response.message() ?: ""
        )
    }

    private fun createErrorResult(code: Int, message: String): GetUsersBySearchResult {
        val data = createErrorData(code, message)
        return GetUsersBySearchResult(data = data, success = false)
    }

    private fun createErrorData(code: Int, message: String?) = UsersData(
        code = code, message = message, pages = null, users = null
    )
}