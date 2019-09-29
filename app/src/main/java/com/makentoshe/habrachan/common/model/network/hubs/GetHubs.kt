package com.makentoshe.habrachan.common.model.network.hubs

import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result

class GetHubs(private val api: HabrApi, private val factory: GetHubsConverterFactory) {

    fun execute(request: GetHubsRequest): Result.GetHubsResponse {
        val response = api.getHubs(
            clientKey = request.clientKey,
            apiKey = request.apiKey,
            accessToken = request.accessToken,
            hubAlias = request.hubAlias,
            page = request.page
        ).execute()

        return if (response.isSuccessful) {
            response.body()!!
        } else {
            factory.converter.convert(response.errorBody()!!)
        }
    }
}