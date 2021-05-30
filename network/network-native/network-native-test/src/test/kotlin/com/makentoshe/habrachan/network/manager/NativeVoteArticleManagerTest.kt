package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.network.deserializer.NativeVoteArticleDeserializer
import com.makentoshe.habrachan.network.request.ArticleVote
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test

class NativeVoteArticleManagerTest: NativeUnitTest() {

    private val manager = NativeVoteArticleManager.Builder(OkHttpClient(), NativeVoteArticleDeserializer()).build()

    @Test
    @Ignore
    fun sas() = runBlocking {
        val request = manager.request(userSession, articleId(442440), ArticleVote.UP)
        val response = manager.vote(request)
    }
}