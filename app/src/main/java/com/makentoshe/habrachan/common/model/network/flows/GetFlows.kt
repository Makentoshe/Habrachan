package com.makentoshe.habrachan.common.model.network.flows

import com.makentoshe.habrachan.common.model.network.ErrorResult
import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result

class GetFlows(private val api: HabrApi) {

    fun execute(request: GetFlowsRequest): Result.GetFlowsResponse {
        val response = api.getFlows(request.clientKey, request.apiKey, request.accessToken).execute()
        return response.body() ?: createErrorResult(response.code(), response.errorBody()?.string() ?: response.message())
    }

    private fun createErrorResult(code: Int, message: String): Result.GetFlowsResponse {
        val errorResult = ErrorResult(code, message)
        return Result.GetFlowsResponse(null, errorResult)
    }
}