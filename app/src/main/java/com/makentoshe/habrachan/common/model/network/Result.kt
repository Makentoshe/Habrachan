package com.makentoshe.habrachan.common.model.network

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.Additional
import com.makentoshe.habrachan.common.model.network.flows.GetFlowsResult
import com.makentoshe.habrachan.common.model.network.hubs.GetHubsResult
import com.makentoshe.habrachan.common.model.network.login.LoginResult
import com.makentoshe.habrachan.common.model.network.posts.byquery.GetPostsByQueryResult
import com.makentoshe.habrachan.common.model.network.posts.GetPostsResult
import com.makentoshe.habrachan.common.model.network.posts.bydate.GetPostsByDateResult
import com.makentoshe.habrachan.common.model.network.posts.bysort.GetPostsBySortResult
import com.makentoshe.habrachan.common.model.network.users.GetUserByLoginResult
import com.makentoshe.habrachan.common.model.network.users.GetUsersBySearchResult
import com.makentoshe.habrachan.common.model.network.votepost.VotePostResult

sealed class Result<T>(val success: T?, val error: ErrorResult?) {

    class GetFlowsResponse(success: GetFlowsResult?, error: ErrorResult?): Result<GetFlowsResult>(success, error)

    class VotePostResponse(success: VotePostResult?, error: ErrorResult?): Result<VotePostResult>(success, error)

    class GetHubsResponse(success: GetHubsResult?, error: ErrorResult?) : Result<GetHubsResult>(success, error)

    class LoginResponse(success: LoginResult?, error: ErrorResult?) : Result<LoginResult>(success, error)

    class GetUserByLoginResponse(
        success: GetUserByLoginResult?, error: ErrorResult?
    ) : Result<GetUserByLoginResult>(success, error)

    class GetUsersBySearchResponse(
        success: GetUsersBySearchResult?, error: ErrorResult?
    ): Result<GetUsersBySearchResult>(success, error)

    class GetPostsByQueryResponse(
        success: GetPostsByQueryResult?, error: ErrorResult?
    ): Result<GetPostsByQueryResult>(success, error)

    class GetPostsResponse(
        success: GetPostsResult?, error: ErrorResult?
    ): Result<GetPostsResult>(success, error)

    class GetPostsBySortResponse(
        success: GetPostsBySortResult?, error: ErrorResult?
    ): Result<GetPostsBySortResult>(success, error)

    class GetPostsByDateResponse(
        success: GetPostsByDateResult?, error: ErrorResult?
    ) : Result<GetPostsByDateResult>(success, error)
}

data class ErrorResult(
    @SerializedName("code", alternate = [])
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("additional")
    private val rawAdditional: Any? = null
) {
    val additional: List<String>
        get() = when (rawAdditional) {
            is Additional -> listOf(rawAdditional.errors)
            is List<*> -> rawAdditional.filterIsInstance<String>()
            else -> listOf()
    }
}
