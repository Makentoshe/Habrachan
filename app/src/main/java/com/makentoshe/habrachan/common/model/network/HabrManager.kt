package com.makentoshe.habrachan.common.model.network

import com.makentoshe.habrachan.common.model.network.flows.GetFlows
import com.makentoshe.habrachan.common.model.network.flows.GetFlowsConverterFactory
import com.makentoshe.habrachan.common.model.network.flows.GetFlowsRequest
import com.makentoshe.habrachan.common.model.network.hubs.GetHubs
import com.makentoshe.habrachan.common.model.network.hubs.GetHubsConverterFactory
import com.makentoshe.habrachan.common.model.network.hubs.GetHubsRequest
import com.makentoshe.habrachan.common.model.network.login.Login
import com.makentoshe.habrachan.common.model.network.login.LoginConverterFactory
import com.makentoshe.habrachan.common.model.network.login.LoginRequest
import com.makentoshe.habrachan.common.model.network.posts.*
import com.makentoshe.habrachan.common.model.network.posts.byquery.GetPostsByQuery
import com.makentoshe.habrachan.common.model.network.posts.byquery.GetPostsByQueryConverterFactory
import com.makentoshe.habrachan.common.model.network.posts.byquery.GetPostsByQueryRequest
import com.makentoshe.habrachan.common.model.network.users.*
import com.makentoshe.habrachan.common.model.network.votepost.VotePost
import com.makentoshe.habrachan.common.model.network.votepost.VotePostConverterFactory
import com.makentoshe.habrachan.common.model.network.votepost.VotePostRequest
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

open class HabrManager(
    protected val api: HabrApi,
    protected val cookieStorage: CookieStorage,
    protected val factories: MutableList<Converter.Factory>
) {

    fun getPosts(request: GetPostsByQueryRequest): Result.GetPostsByQueryResponse {
        val factory = factories.find {
            it::class.java == GetPostsByQueryConverterFactory::class.java
        }.let {
            it as GetPostsByQueryConverterFactory
        }
        return GetPostsByQuery(api, factory).execute(request)
    }

    fun getPosts(request: GetPostsRequest): Result.GetPostsResponse{
        val factory = factories.find {
            it::class.java == GetPostsConverterFactory::class.java
        }.let {
            it as GetPostsConverterFactory
        }
        return GetPosts(api, factory).execute(request)
    }

    fun login(request: LoginRequest): Result.LoginResponse {
        val factory = factories.find {
            it::class.java == LoginConverterFactory::class.java
        }.let {
            it as LoginConverterFactory
        }
        return Login(api, factory).execute(request)
    }

    fun votePost(request: VotePostRequest): Result.VotePostResponse {
        val factory = factories.find {
            it::class.java == VotePostConverterFactory::class.java
        }.let {
            it as VotePostConverterFactory
        }
        return VotePost(api, cookieStorage, factory).execute(request)
    }

    fun getUsers(request: GetUsersBySearchRequest): Result.GetUsersBySearchResponse{
        val factory = factories.find {
            it::class.java == GetUsersBySearchConverterFactory::class.java
        }.let {
            it as GetUsersBySearchConverterFactory
        }
        return GetUsersBySearch(api, factory).execute(request)
    }

    fun getUser(request: GetUserByLoginRequest): Result.GetUserByLoginResponse {
        val factory = factories.find {
            it::class.java == GetUserByLoginConverterFactory::class.java
        }.let {
            it as GetUserByLoginConverterFactory
        }
        return GetUserByLogin(api, factory).execute(request)
    }

    fun getFlows(request: GetFlowsRequest): Result.GetFlowsResponse {
        return GetFlows(api).execute(request)
    }

    fun getHubs(request: GetHubsRequest): Result.GetHubsResponse {
        val factory = factories.find {
            it::class.java == GetHubsConverterFactory::class.java
        }.let {
            it as GetHubsConverterFactory
        }
        return GetHubs(api, factory).execute(request)
    }

    companion object {
        fun build(
            cookie: SessionCookie = SessionCookie(),
            client: OkHttpClient = OkHttpClient.Builder().cookieJar(cookie).build(),
            baseUrl: String = "https://habr.com/"
        ): HabrManager {
            val retrofit =
                Retrofit.Builder().client(client).baseUrl(baseUrl)
                    .addConverterFactory(GetPostsConverterFactory())
                    .addConverterFactory(StringConverterFactory())
                    .addConverterFactory(ByteArrayConverterFactory())
                    .addConverterFactory(LoginConverterFactory())
                    .addConverterFactory(VotePostConverterFactory())
                    .addConverterFactory(GetUsersBySearchConverterFactory())
                    .addConverterFactory(GetUserByLoginConverterFactory())
                    .addConverterFactory(GetPostsByQueryConverterFactory())
                    .addConverterFactory(GetFlowsConverterFactory())
                    .addConverterFactory(GetHubsConverterFactory())
                    .build()
            return HabrManager(retrofit.create(HabrApi::class.java), cookie, retrofit.converterFactories())
        }
    }
}
