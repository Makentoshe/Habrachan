package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.NativeArticlesApi
import com.makentoshe.habrachan.network.deserializer.NativeVoteArticleDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.ArticleVote
import com.makentoshe.habrachan.network.request.NativeVoteArticleRequest
import com.makentoshe.habrachan.network.response.VoteArticleResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

open class NativeVoteArticleManager(
    private val api: NativeArticlesApi, private val deserializer: NativeVoteArticleDeserializer
) : VoteArticleManager<NativeVoteArticleRequest> {

    override fun request(userSession: UserSession, articleId: ArticleId, vote: ArticleVote): NativeVoteArticleRequest {
        return NativeVoteArticleRequest(articleId, userSession, vote)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun vote(request: NativeVoteArticleRequest): kotlin.Result<VoteArticleResponse> = try {
        when (request.articleVote) {
            ArticleVote.UP -> {
                api.voteArticleUp(request.userSession.client, request.userSession.token, request.articleId.articleId)
            }
            ArticleVote.DOWN -> {
                api.voteArticleDown(request.userSession.client, request.userSession.token, request.articleId.articleId)
            }
        }.execute().fold({
            deserializer.success(request, it.string())
        }, {
            deserializer.failure(request, it.string())
        }).fold({
            kotlin.Result.success(it)
        },{
            kotlin.Result.failure(it)
        })
    } catch (exception: Exception) {
        kotlin.Result.failure(exception)
    }

    class Builder(private val client: OkHttpClient, private val deserializer: NativeVoteArticleDeserializer) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeVoteArticleManager(getRetrofit().create(NativeArticlesApi::class.java), deserializer)
    }
}