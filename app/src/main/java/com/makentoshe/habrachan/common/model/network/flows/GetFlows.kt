package com.makentoshe.habrachan.common.model.network.flows

import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result

class GetFlows(
    private val api: HabrApi,
    private val factory: GetFlowsConverterFactory
) {

    fun execute(request: GetFlowsRequest): Result.GetFlowsResponse {
        val response = api.getFlows(request.clientKey, request.apiKey, request.accessToken).execute()
        return if (response.isSuccessful) {
            response.body()!!
        } else {
            factory.converter.convert(response.errorBody()!!)
        }
    }
}