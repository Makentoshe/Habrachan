package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.manager.VoteArticleManager
import com.makentoshe.habrachan.network.natives.api.NativeArticlesApi
import com.makentoshe.habrachan.network.request.ArticleVote
import com.makentoshe.habrachan.network.response.VoteArticleResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class NativeVoteArticleManager private constructor(
    private val api: NativeArticlesApi, private val deserializer: NativeVoteArticleDeserializer
) : VoteArticleManager<NativeVoteArticleRequest> {

    override fun request(userSession: UserSession, articleId: ArticleId, vote: ArticleVote): NativeVoteArticleRequest {
        return NativeVoteArticleRequest(articleId, userSession, vote)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun vote(request: NativeVoteArticleRequest): Result<VoteArticleResponse> = try {
        when (val articleVote = request.articleVote) {
            is ArticleVote.Up -> {
                api.voteArticleUp(request.userSession.client, request.userSession.token, request.articleId.articleId)
            }
            is ArticleVote.Down -> {
                api.voteArticleDown(
                    request.userSession.client,
                    request.userSession.token,
                    request.articleId.articleId,
                    articleVote.reason.ordinal + 1
                )
            }
        }.execute().run {
            fold({
                deserializer.success(request, it.string(), code(), message())
            }, {
                deserializer.failure(request, it.string(), code(), message())
            })
        }
    } catch (exception: Exception) {
        val additional = listOf(exception.localizedMessage)
        Result.failure(NativeVoteArticleException(request, "", additional, -1, exception.localizedMessage, exception))
    }

    class Builder(private val client: OkHttpClient) {

        private val deserializer = NativeVoteArticleDeserializer()
        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeVoteArticleManager(getRetrofit().create(NativeArticlesApi::class.java), deserializer)
    }
}