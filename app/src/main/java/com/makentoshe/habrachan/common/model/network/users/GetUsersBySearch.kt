package com.makentoshe.habrachan.common.model.network.users

import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result

class GetUsersBySearch(
    private val api: HabrApi,
    private val factory: GetUsersBySearchConverterFactory
) {

    fun execute(request: GetUsersBySearchRequest): Result.GetUsersBySearchResponse{
        val response = api.getUsersBySearch(page = request.page, query = request.query).execute()

        return if (response.isSuccessful) {
            response.body()!!
        } else {
            factory.converter.convert(response.errorBody()!!)
        }
    }
}