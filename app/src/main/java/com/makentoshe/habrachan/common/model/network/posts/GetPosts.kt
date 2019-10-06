package com.makentoshe.habrachan.common.model.network.posts

import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result

class GetPosts(
    private val api: HabrApi,
    private val factory: GetPostsConverterFactory
) {
    fun execute(request: GetPostsRequest): Result.GetPostsResponse{
        val response = api.getPosts(
            page = request.page,
            sort = request.sort,
            hl = request.hl,
            fl = request.fl,
            date = request.date,
            custom = request.custom
        ).execute()

        return if (response.isSuccessful) {
            response.body()!!
        } else {
            factory.converter.convert(response.errorBody()!!)
        }
    }


}