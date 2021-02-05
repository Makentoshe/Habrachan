package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.api.NativeArticlesApi
import com.makentoshe.habrachan.network.converter.ArticleConverter
import com.makentoshe.habrachan.network.converter.ArticlesConverter
import com.makentoshe.habrachan.network.converter.VoteUpArticleConverter
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.GetArticleRequest
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.request.UserArticlesRequest
import com.makentoshe.habrachan.network.request.VoteArticleRequest
import com.makentoshe.habrachan.network.response.ArticleResponse
import com.makentoshe.habrachan.network.response.GetArticlesResponse
import com.makentoshe.habrachan.network.response.VoteArticleResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface ArticlesManager {

    suspend fun getArticles(request: GetArticlesRequest): Result<GetArticlesResponse>

    suspend fun getUserArticles(request: UserArticlesRequest): Result<GetArticlesResponse>

    suspend fun getArticle(request: GetArticleRequest): Result<ArticleResponse>

    suspend fun voteUp(request: VoteArticleRequest): Result<VoteArticleResponse>

    suspend fun voteDown(request: VoteArticleRequest): Result<VoteArticleResponse>

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun native() = NativeArticlesManager(getRetrofit().create(NativeArticlesApi::class.java))
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
class NativeArticlesManager(private val api: NativeArticlesApi) : ArticlesManager {

    override suspend fun getArticles(request: GetArticlesRequest): Result<GetArticlesResponse> {
        return api.getArticles(
            request.session.client,
            request.session.api,
            request.session.token,
            request.spec.request,
            request.page,
            request.spec.include,
            request.spec.exclude,
            request.spec.sort
        ).execute().fold({
            ArticlesConverter().convertBody(it)
        }, {
            ArticlesConverter().convertError(it)
        })
    }

    override suspend fun getUserArticles(request: UserArticlesRequest): Result<GetArticlesResponse> {
        return api.getUserArticles(
            request.session.client,
            request.session.token,
            request.user,
            request.page,
            request.include,
            request.exclude
        ).execute().fold({
            ArticlesConverter().convertBody(it)
        }, {
            ArticlesConverter().convertError(it)
        })
    }

    override suspend fun getArticle(request: GetArticleRequest): Result<ArticleResponse> {
        return api.getArticle(
            request.session.client,
            request.session.api,
            request.session.token,
            request.id
        ).execute().fold({
            ArticleConverter().convertBody(it)
        }, {
            ArticleConverter().convertError(it)
        })
    }

    override suspend fun voteUp(request: VoteArticleRequest): Result<VoteArticleResponse> {
        return api.voteUp(request.clientKey, request.tokenKey, request.articleId).execute().fold({
            VoteUpArticleConverter().convertBody(it)
        }, {
            VoteUpArticleConverter().convertError(it)
        })
    }

    override suspend fun voteDown(request: VoteArticleRequest): Result<VoteArticleResponse> {
        return api.voteDown(request.clientKey, request.tokenKey, request.articleId).execute().fold({
            VoteUpArticleConverter().convertBody(it)
        }, {
            VoteUpArticleConverter().convertError(it)
        })
    }
}
