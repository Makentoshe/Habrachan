package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.network.deserializer.NativeVoteArticleDeserializer
import com.makentoshe.habrachan.network.request.ArticleVote
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class NativeVoteArticleManagerTest: NativeUnitTest() {

    private val manager = NativeVoteArticleManager.Builder(OkHttpClient(), NativeVoteArticleDeserializer()).build()

    @Test
    @Ignore
    fun success() = runBlocking {
        val userSession = userSession(client, api, "")
        val request = manager.request(userSession, articleId(559980), ArticleVote.UP)
        val response = manager.vote(request)

        println(response)
    }
}