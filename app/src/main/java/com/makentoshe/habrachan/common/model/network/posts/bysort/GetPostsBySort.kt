package com.makentoshe.habrachan.common.model.network.posts.bysort

import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result

class GetPostsBySort(
    private val api: HabrApi,
    private val factory: GetPostsBySortConverterFactory
) {
    fun execute(request: GetPostsBySortRequest): Result.GetPostsBySortResponse {
        val response = api.getPostsBySort(
            sort = request.sort,
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
