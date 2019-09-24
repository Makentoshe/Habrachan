package com.makentoshe.habrachan.common.model.network

import com.makentoshe.habrachan.common.model.entity.GetPostsBySearchResult

class GetPostsBySearch(
    private val api: HabrApi
) {
    fun execute(request: GetPostsBySearchRequest): GetPostsBySearchResult {
        if (request.page < 1) {
            throw IllegalArgumentException()
        }
        val response = api.getPostsBySearch(
            query = request.query, page = request.page, hl = request.hl, fl = request.fl
        ).execute()

        // todo if body is null
        return response.body()!!
    }
}
