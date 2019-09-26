package com.makentoshe.habrachan.common.model.network

import com.makentoshe.habrachan.common.model.network.login.Login
import com.makentoshe.habrachan.common.model.network.login.LoginRequest
import com.makentoshe.habrachan.common.model.network.login.LoginResult
import com.makentoshe.habrachan.common.model.network.posts.*
import com.makentoshe.habrachan.common.model.network.votepost.VotePost
import com.makentoshe.habrachan.common.model.network.votepost.VotePostRequest
import com.makentoshe.habrachan.common.model.network.votepost.VotePostResult
import okhttp3.OkHttpClient
import retrofit2.Retrofit

open class HabrManager(
    protected val api: HabrApi, protected val cookieStorage: CookieStorage
) {

    fun getPostsBySearch(request: GetPostsBySearchRequest): GetPostsResult {
        return GetPostsBySearch(api).execute(request)
    }

    fun getPosts(request: GetPostsRequest): GetPostsResult {
        return GetPosts(api).execute(request)
    }

    fun login(request: LoginRequest): LoginResult {
        return Login(api, cookieStorage).execute(request)
    }

    fun votePost(request: VotePostRequest): VotePostResult {
        return VotePost(api, cookieStorage).execute(request)
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
                    .build()
            return HabrManager(retrofit.create(HabrApi::class.java), cookie)
        }
    }
}
