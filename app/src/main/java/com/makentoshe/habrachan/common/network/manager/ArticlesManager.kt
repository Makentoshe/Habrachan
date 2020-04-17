package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.entity.article.ArticleResponse
import com.makentoshe.habrachan.common.entity.article.ArticlesResponse
import com.makentoshe.habrachan.common.entity.article.VoteArticleResponse
import com.makentoshe.habrachan.common.network.api.NativeArticlesApi
import com.makentoshe.habrachan.common.network.converter.ArticleConverter
import com.makentoshe.habrachan.common.network.converter.ArticlesConverter
import com.makentoshe.habrachan.common.network.converter.VoteUpArticleConverter
import com.makentoshe.habrachan.common.network.request.GetArticleRequest
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import com.makentoshe.habrachan.common.network.request.VoteArticleRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface ArticlesManager {

    fun getArticles(request: GetArticlesRequest): Single<ArticlesResponse>

    fun getArticle(request: GetArticleRequest): Single<ArticleResponse>

    fun voteUp(request: VoteArticleRequest): Single<VoteArticleResponse>

    fun voteDown(request: VoteArticleRequest): Single<VoteArticleResponse>

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build(): ArticlesManager {
            val api = getRetrofit().create(NativeArticlesApi::class.java)
            return NativeArticlesManager(api)
        }
    }
}

class NativeArticlesManager(private val api: NativeArticlesApi) : ArticlesManager {

    override fun getArticles(request: GetArticlesRequest): Single<ArticlesResponse> {
        return Single.just(request).observeOn(Schedulers.io()).map { request ->
            val spec = request.spec.request
            api.getArticles(request.client, request.api, request.token, spec, request.page, request.include, request.exclude).execute()
        }.map { response ->
            if (response.isSuccessful) {
                ArticlesConverter().convertBody(response.body()!!)
            } else {
                ArticlesConverter().convertError(response.errorBody()!!)
            }
        }.cast(ArticlesResponse::class.java)
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
