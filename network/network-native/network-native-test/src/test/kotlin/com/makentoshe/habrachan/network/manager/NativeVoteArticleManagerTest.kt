package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.network.request.ArticleVote
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class NativeVoteArticleManagerTest : NativeUnitTest() {

    private val manager = NativeVoteArticleManager.Builder(OkHttpClient()).build()

    @Test
    @Ignore
    fun successVoteUp() = runBlocking {
        val userSession = userSession(client, api, "")
        val request = manager.request(userSession, articleId(-1), ArticleVote.Up)
        val response = manager.vote(request)

        println(response)
    }

    @Test
    @Ignore
    fun successVoteDown() = runBlocking {
        val userSession = userSession(client, api, "")
        val request = manager.request(userSession, articleId(-1), ArticleVote.Down(ArticleVote.Down.Reason.OTHER))
        val response = manager.vote(request)

        println(response)
    }
}