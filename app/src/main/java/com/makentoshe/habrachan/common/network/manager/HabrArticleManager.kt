package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.entity.article.VoteArticleResponse
import com.makentoshe.habrachan.common.entity.post.ArticleResponse
import com.makentoshe.habrachan.common.entity.post.ArticlesResponse
import com.makentoshe.habrachan.common.entity.post.PostResponse
import com.makentoshe.habrachan.common.network.api.HabrArticlesApi
import com.makentoshe.habrachan.common.network.converter.*
import com.makentoshe.habrachan.common.network.request.GetArticleRequest
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import com.makentoshe.habrachan.common.network.request.VoteArticleRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

interface HabrArticleManager {

    fun getArticles(request: GetArticlesRequest): Single<ArticlesResponse>

    fun getPost(request: GetArticleRequest): Single<PostResponse>

    fun getArticle(request: GetArticleRequest): Single<ArticleResponse>

    fun voteUp(request: VoteArticleRequest): Single<VoteArticleResponse>

    fun voteDown(request: VoteArticleRequest): Single<VoteArticleResponse>

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(PostsConverterFactory())
            .addConverterFactory(PostConverterFactory())
            .build()

        fun build(include: String? = null): HabrArticleManager {
            val retrofit = getRetrofit()
            val api = retrofit.create(HabrArticlesApi::class.java)
            return object : HabrArticleManager {

                override fun getArticles(request: GetArticlesRequest): Single<ArticlesResponse> {
                    return Single.just(request).observeOn(Schedulers.io()).map { request ->
                        api.getArticles(request.client, request.api, request.token, request.spec, request.page).execute()
                    }.map { response ->
                        if (response.isSuccessful) {
                            ArticlesConverter().convertBody(response.body()!!)
                        } else {
                            ArticlesConverter().convertError(response.errorBody()!!)
                        }
                    }.cast(ArticlesResponse::class.java)
                }

                override fun getPost(request: GetArticleRequest): Single<PostResponse> {
                    return api.getPost(
                        request.client, request.token, request.api, request.id, include, null, null
                    )
                }

                override fun getArticle(request: GetArticleRequest): Single<ArticleResponse> {
                    return Single.just(request).observeOn(Schedulers.io()).map { request ->
                        api.getArticle(request.client, request.api, request.token, request.id).execute()
                    }.map { response ->
                        if (response.isSuccessful) {
                            ArticleConverter().convertBody(response.body()!!)
                        } else {
                            ArticleConverter().convertError(response.errorBody()!!)
                        }
                    }
                }

                override fun voteUp(request: VoteArticleRequest): Single<VoteArticleResponse> {
                    return Single.just(request).observeOn(Schedulers.io()).map { request ->
                        api.voteUp(request.clientKey, request.tokenKey, request.articleId).execute()
                    }.map { response ->
                        if (response.isSuccessful) {
                            VoteUpArticleConverter().convertBody(response.body()!!)
                        } else {
                            VoteUpArticleConverter().convertError(response.errorBody()!!)
                        }
                    }
                }

                override fun voteDown(request: VoteArticleRequest): Single<VoteArticleResponse> {
                    return Single.just(request).observeOn(Schedulers.io()).map { request ->
                        api.voteDown(request.clientKey, request.tokenKey, request.articleId).execute()
                    }.map { response ->
                        if (response.isSuccessful) {
                            VoteUpArticleConverter().convertBody(response.body()!!)
                        } else {
                            VoteUpArticleConverter().convertError(response.errorBody()!!)
                        }
                    }
                }
            }
        }
    }
}
