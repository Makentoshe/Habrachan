package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.functional.getOrThrow
import com.makentoshe.habrachan.network.deserializer.NativeVoteArticleDeserializer
import com.makentoshe.habrachan.network.exception.NativeVoteArticleException
import com.makentoshe.habrachan.network.request.ArticleVote
import com.makentoshe.habrachan.network.request.NativeVoteArticleRequest
import org.junit.Assert.assertEquals
import org.junit.Test

class NativeVoteArticleDeserializerTest : UnitTest() {

    private val deserializer = NativeVoteArticleDeserializer()

    @Test
    fun testShouldCheckSuccessDeserialize() {
        val successJson = getResourceString("vote_article_success.json")
        val request = NativeVoteArticleRequest(articleId(442440), userSession, ArticleVote.Up)
        val result = deserializer.success(request, successJson, 1, "message")

        val response = result.getOrThrow()

        assertEquals(request, response.request)
        assertEquals(true, response.ok)
        assertEquals(4, response.score)
        assertEquals("2021-05-29T01:11:25+03:00", response.serverTime)
    }

    @Test
    fun testShouldCheckSuccessDeserializeWithFailureResponse() {
        val failureJson = getResourceString("vote_article_failure_token.json")
        val request = NativeVoteArticleRequest(articleId(442440), userSession, ArticleVote.Up)
        val result = deserializer.failure(request, failureJson, 900, "error")

        val exception = result.exceptionOrNull()!! as NativeVoteArticleException
        assertEquals(failureJson, exception.raw)
        assertEquals(request, exception.request)
        assertEquals(401, exception.code)
        assertEquals("Authorization required", exception.message)
    }

    @Test
    fun testShouldCheckFailureDeserialize() {
        val failureJson = "Any failure json that should not being parsed successfully at all"
        val request = NativeVoteArticleRequest(articleId(442440), userSession, ArticleVote.Up)
        val result = deserializer.failure(request, failureJson, 900, "error")

        val exception = result.exceptionOrNull()!! as NativeVoteArticleException
        assertEquals(failureJson, exception.raw)
        assertEquals(request, exception.request)
        assertEquals(900, exception.code)
        assertEquals("error", exception.message)
    }
}