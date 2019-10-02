package com.makentoshe.habrachan.common.model.network.posts.bydate

import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result

class GetPostsByDate(
    private val api: HabrApi, private val factory: GetPostsByDateConverterFactory
) {
    fun execute(request: GetPostsByDateRequest): Result.GetPostsByDateResponse {
        val response = api.getPostsByDate(
            date = request.date,
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

