package com.makentoshe.habrachan.common.model.network.postsalt

import com.makentoshe.habrachan.common.model.network.StringConverterFactory
import com.makentoshe.habrachan.common.model.network.postsalt.entity.PostsResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class HabrPostsManager(
    private val api: HabrPostsApi
) {

    fun getRaw(request: GetRawRequest): Single<PostsResponse> {
        return api.getRawPosts(
            clientKey = request.client,
            token = request.token,
            apiKey = request.api,
            type1 = request.path1,
            type2 = request.path2,
            page = request.page,
            include = null,
            getArticle = null,
            exclude = null
        )
    }

    fun getInteresting(request: GetInterestingRequest): Single<PostsResponse> {
        return api.getPosts(request.client, request.token, request.api, "interesting", request.page, null, null, null)
    }

    fun getAll(request: GetAllRequest): Single<PostsResponse> {
        return api.getPosts(request.client, request.token, request.api, "all", request.page, null, null, null)
    }

    fun getTop(request: GetTopRequest): Single<PostsResponse> {
        return api.getTopPosts(request.client, request.token, request.api, request.type, request.page, null, null, null)
    }

    fun getFeed(request: GetFeedRequest): Single<PostsResponse> {
        return api.getFeed(request.client, request.token, request.page, null, null, null)
    }

    companion object {
        fun build(
            client: OkHttpClient = OkHttpClient.Builder().build(), baseUrl: String = "https://habr.com/"
        ): HabrPostsManager {
            val retrofit =
                Retrofit.Builder().client(client).baseUrl(baseUrl).addConverterFactory(StringConverterFactory())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(PostsConverterFactory()).build()
            return HabrPostsManager(retrofit.create(HabrPostsApi::class.java))
        }

    }
}
