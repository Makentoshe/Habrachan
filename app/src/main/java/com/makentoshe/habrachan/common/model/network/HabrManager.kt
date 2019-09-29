package com.makentoshe.habrachan.common.model.network

import com.makentoshe.habrachan.common.model.network.flows.GetFlows
import com.makentoshe.habrachan.common.model.network.flows.GetFlowsConverterFactory
import com.makentoshe.habrachan.common.model.network.flows.GetFlowsRequest
import com.makentoshe.habrachan.common.model.network.login.Login
import com.makentoshe.habrachan.common.model.network.login.LoginConverterFactory
import com.makentoshe.habrachan.common.model.network.login.LoginRequest
import com.makentoshe.habrachan.common.model.network.login.LoginResult
import com.makentoshe.habrachan.common.model.network.posts.*
import com.makentoshe.habrachan.common.model.network.users.*
import com.makentoshe.habrachan.common.model.network.votepost.VotePost
import com.makentoshe.habrachan.common.model.network.votepost.VotePostConverterFactory
import com.makentoshe.habrachan.common.model.network.votepost.VotePostRequest
import com.makentoshe.habrachan.common.model.network.votepost.VotePostResult
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

open class HabrManager(
    protected val api: HabrApi,
    protected val cookieStorage: CookieStorage,
    protected val factories: MutableList<Converter.Factory>
) {

    fun getPostsBySearch(request: GetPostsBySearchRequest): GetPostsBySearchResult {
        return GetPostsBySearch(api).execute(request)
    }

    fun getPosts(request: GetPostsRequest): GetPostsResult {
        return GetPosts(api).execute(request)
    }

    fun login(request: LoginRequest): LoginResult {
        return Login(api, cookieStorage).execute(request)
    }

    fun votePost(request: VotePostRequest): Result.VotePostResponse {
        val factory = factories.find {
            it::class.java == VotePostConverterFactory::class.java
        }.let {
            it as VotePostConverterFactory
        }
        return VotePost(api, cookieStorage, factory).execute(request)
    }

    fun getUsersBySearch(request: GetUsersBySearchRequest): GetUsersBySearchResult {
        return GetUsersBySearch(api).execute(request)
    }

    fun getUserByLogin(request: GetUserByLoginRequest): GetUserByLoginResult {
        return GetUserByLogin(api).execute(request)
    }

    fun getFlows(request: GetFlowsRequest): Result.GetFlowsResponse {
        return GetFlows(api).execute(request)
    }

    companion object {
        fun build(
            cookie: SessionCookie = SessionCookie(),
            client: OkHttpClient = OkHttpClient.Builder().cookieJar(cookie).build(),
            baseUrl: String = "https://habr.com/"
        ): HabrManager {
            val retrofit =
                Retrofit.Builder().client(client).baseUrl(baseUrl)
                    .addConverterFactory(PostsConverterFactory())
                    .addConverterFactory(StringConverterFactory())
                    .addConverterFactory(ByteArrayConverterFactory())
                    .addConverterFactory(LoginConverterFactory())
                    .addConverterFactory(VotePostConverterFactory())
                    .addConverterFactory(GetUsersBySearchConverterFactory())
                    .addConverterFactory(GetUserByLoginConverterFactory())
                    .addConverterFactory(PostsBySearchConverterFactory())
                    .addConverterFactory(GetFlowsConverterFactory())
                    .build()
            return HabrManager(retrofit.create(HabrApi::class.java), cookie, retrofit.converterFactories())
        }
    }
}
