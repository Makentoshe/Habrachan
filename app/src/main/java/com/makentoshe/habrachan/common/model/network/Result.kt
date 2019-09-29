package com.makentoshe.habrachan.common.model.network

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.network.flows.GetFlowsResult
import com.makentoshe.habrachan.common.model.network.votepost.VotePostResult

sealed class Result<T>(val success: T?, val error: ErrorResult?) {
    class GetFlowsResponse(success: GetFlowsResult?, error: ErrorResult?): Result<GetFlowsResult>(success, error)
    class VotePostResponse(success: VotePostResult?, error: ErrorResult?): Result<VotePostResult>(success, error)
}

data class ErrorResult(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("additional")
    val additional: List<String> = emptyList()
)
