package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.entity.article.VoteUpArticleResponse
import com.makentoshe.habrachan.common.entity.post.PostResponse
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.common.network.api.HabrArticlesApi
import com.makentoshe.habrachan.common.network.converter.PostConverterFactory
import com.makentoshe.habrachan.common.network.converter.PostsConverterFactory
import com.makentoshe.habrachan.common.network.converter.VoteUpArticleConverterFactory
import com.makentoshe.habrachan.common.network.request.GetArticleRequest
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import com.makentoshe.habrachan.common.network.request.VoteUpArticleRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

interface HabrArticleManager {

    fun getPosts(request: GetArticlesRequest): Single<PostsResponse>

    fun getPost(request: GetArticleRequest): Single<PostResponse>

    fun voteUp(request: VoteUpArticleRequest): Single<VoteUpArticleResponse>

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(PostsConverterFactory())
            .addConverterFactory(PostConverterFactory())
            .addConverterFactory(VoteUpArticleConverterFactory())
            .build()

        fun build(include: String? = null): HabrArticleManager {
            val retrofit = getRetrofit()
            val api = retrofit.create(HabrArticlesApi::class.java)
            return object: HabrArticleManager {

                override fun getPosts(request: GetArticlesRequest): Single<PostsResponse> {
                    return api.getPosts(
                        request.client, request.token, request.api, request.path1, request.path2, request.page, include
                    )
                }

                override fun getPost(request: GetArticleRequest): Single<PostResponse> {
                    return api.getPost(
                        request.client, request.token, request.api, request.id, include, null, null
                    )
                }

                override fun voteUp(request: VoteUpArticleRequest): Single<VoteUpArticleResponse> {
                    return api.voteUp(request.clientKey, request.tokenKey, request.articleId)
                }
            }
        }
    }
}
