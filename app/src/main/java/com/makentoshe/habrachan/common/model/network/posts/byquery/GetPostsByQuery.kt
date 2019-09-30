package com.makentoshe.habrachan.common.model.network.posts.byquery

import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result

class GetPostsByQuery(
    private val api: HabrApi, private val factory: GetPostsByQueryConverterFactory
) {
    fun execute(request: GetPostsByQueryRequest): Result.GetPostsByQueryResponse {
        val response = api.getPostsByQuery(
            query = request.query,
            page = request.page,
            hl = request.hl,
            fl = request.fl
        ).execute()

        return if (response.isSuccessful) {
            response.body()!!
        } else {
            factory.converter.convert(response.errorBody()!!)
        }
    }
}

