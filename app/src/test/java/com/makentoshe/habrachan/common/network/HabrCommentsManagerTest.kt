package com.makentoshe.habrachan.common.network

import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.MockInterceptor
import com.makentoshe.habrachan.common.entity.comment.VoteCommentResponse
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.request.VoteCommentRequest
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class HabrCommentsManagerTest : BaseTest() {

    @Test
    fun `should parse and return success result for up voting action`() {
        val commentId = 10001
        val url = "https://habr.com/api/v1/comments/$commentId/vote?vote=1"
        val json = "{\"ok\":true,\"score\":4,\"server_time\":\"2020-03-18T17:52:25+03:00\"}"
        val request = VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
        val client = OkHttpClient.Builder().addInterceptor(MockInterceptor(url, 200, json)).build()
        val manager = HabrCommentsManager.Factory(client).build()
        val response = manager.voteUp(request).blockingGet() as VoteCommentResponse.Success
        Assert.assertEquals(4, response.score)
        Assert.assertEquals("2020-03-18T17:52:25+03:00", response.serverTime)
    }

    @Test
    fun `should parse and return success result for down voting action`() {
        val commentId = 10101
        val url = "https://habr.com/api/v1/comments/$commentId/vote?vote=-1"
        val json = "{\"ok\":true,\"score\":-5,\"server_time\":\"2020-03-18T17:52:25+03:00\"}"

        val request = VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
        val client = OkHttpClient.Builder().addInterceptor(MockInterceptor(url, 200, json)).build()
        val manager = HabrCommentsManager.Factory(client).build()
        val response = manager.voteDown(request).blockingGet() as VoteCommentResponse.Success

        Assert.assertEquals(-5, response.score)
        Assert.assertEquals("2020-03-18T17:52:25+03:00", response.serverTime)
    }

    @Test
    fun `should parse and return error result for up voting action`() {
        val commentId = 1
        val url = "https://habr.com/api/v1/comments/$commentId/vote?vote=1"
        val json = "{\"code\":400,\"message\":\"Incorrect parameters\",\"additional\":[\"Повторное голосование запрещено\"],\"data\":[]}"

        val request = VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
        val client = OkHttpClient.Builder().addInterceptor(MockInterceptor(url, 400, json)).build()
        val manager = HabrCommentsManager.Factory(client).build()
        val response = manager.voteUp(request).blockingGet() as VoteCommentResponse.Error

        Assert.assertEquals(400, response.code)
        Assert.assertEquals("Incorrect parameters", response.message)
        Assert.assertEquals(listOf("Повторное голосование запрещено"), response.additional)
    }

    @Test
    fun `should parse and return error result for down voting action`() {
        val commentId = 1
        val url = "https://habr.com/api/v1/comments/$commentId/vote?vote=-1"
        val json = "{\"code\":400,\"message\":\"Incorrect parameters\",\"additional\":[\"Повторное голосование запрещено\"],\"data\":[]}"

        val request = VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
        val client = OkHttpClient.Builder().addInterceptor(MockInterceptor(url, 400, json)).build()
        val manager = HabrCommentsManager.Factory(client).build()
        val response = manager.voteDown(request).blockingGet() as VoteCommentResponse.Error

        Assert.assertEquals(400, response.code)
        Assert.assertEquals("Incorrect parameters", response.message)
        Assert.assertEquals(listOf("Повторное голосование запрещено"), response.additional)
    }

    @Test
    @Ignore("Uses real api")
    fun voteUpCommentTest() {
        val commentId = 21395226
        val request = VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
        val manager = HabrCommentsManager.Factory(OkHttpClient()).build()
        val response = manager.voteUp(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore("Uses real api")
    fun voteDownCommentTest() {
        val commentId = 21396134
        val request = VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
        val manager = HabrCommentsManager.Factory(OkHttpClient()).build()
        val response = manager.voteDown(request).blockingGet()
        println(response)
    }

}