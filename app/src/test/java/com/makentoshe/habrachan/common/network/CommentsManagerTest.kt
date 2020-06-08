package com.makentoshe.habrachan.common.network

import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.ResponseInterceptor
import com.makentoshe.habrachan.UrlInterceptor
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import com.makentoshe.habrachan.common.network.request.SendCommentRequest
import com.makentoshe.habrachan.common.network.request.VoteCommentRequest
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.common.network.response.SendCommentResponse
import com.makentoshe.habrachan.common.network.response.VoteCommentResponse
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class CommentsManagerTest : BaseTest() {

    @Test
    fun `should parse and return success result for up voting action`() {
        val commentId = 10001
        val url = "https://habr.com/api/v1/comments/$commentId/vote?vote=1"
        val json = getJsonResponse("vote_comment_success.json")

        val request = VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(200, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = CommentsManager.Factory(client).buildNative()
        val response = manager.voteUp(request).blockingGet() as VoteCommentResponse.Success

        Assert.assertEquals(2077, response.score)
        Assert.assertEquals("2020-03-18T17:52:25+03:00", response.serverTime)
        Assert.assertEquals(request, response.request)
    }

    @Test
    fun `should parse and return success result for down voting action`() {
        val commentId = 10101
        val url = "https://habr.com/api/v1/comments/$commentId/vote?vote=-1"
        val json = getJsonResponse("vote_comment_success.json")

        val request = VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(200, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = CommentsManager.Factory(client).buildNative()
        val response = manager.voteDown(request).blockingGet() as VoteCommentResponse.Success

        Assert.assertEquals(2077, response.score)
        Assert.assertEquals("2020-03-18T17:52:25+03:00", response.serverTime)
        Assert.assertEquals(request, response.request)
    }

    @Test
    fun `should parse and return error result for up voting action`() {
        val commentId = 1
        val url = "https://habr.com/api/v1/comments/$commentId/vote?vote=1"
        val json = getJsonResponse("vote_comment_error.json")

        val request = VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(400, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = CommentsManager.Factory(client).buildNative()
        val response = manager.voteUp(request).blockingGet() as VoteCommentResponse.Error

        Assert.assertEquals(696, response.code)
        Assert.assertEquals("Message", response.message)
        Assert.assertEquals(listOf("Additional1", "Additional2"), response.additional)
        Assert.assertEquals(request, response.request)
    }

    @Test
    fun `should parse and return error result for down voting action`() {
        val commentId = 1
        val url = "https://habr.com/api/v1/comments/$commentId/vote?vote=-1"
        val json = getJsonResponse("vote_comment_error.json")

        val request = VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(400, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = CommentsManager.Factory(client).buildNative()
        val response = manager.voteDown(request).blockingGet() as VoteCommentResponse.Error

        Assert.assertEquals(696, response.code)
        Assert.assertEquals("Message", response.message)
        Assert.assertEquals(listOf("Additional1", "Additional2"), response.additional)
        Assert.assertEquals(request, response.request)
    }

    @Test
    fun `should parse and return success result for get comments action`() {
        val articleId = 101
        val url = "https://habr.com/api/v1/comments/$articleId?since=-1"
        val json = getJsonResponse("get_comments_success.json")

        val request = GetCommentsRequest(session.clientKey, session.apiKey, session.tokenKey, articleId)
        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(200, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = CommentsManager.Factory(client).buildNative()
        val response = manager.getComments(request).blockingGet() as GetCommentsResponse.Success

        Assert.assertEquals(19, response.data.size)
        Assert.assertEquals(19882394, response.last)
        Assert.assertEquals("2020-03-19T05:55:09+03:00", response.serverTime)
    }

    @Test
    fun testShouldParseAndReturnErrorResultForGetCommentAction() {
        val articleId = 101
        val url = "https://habr.com/api/v1/comments/$articleId?since=-1"
        val json = getJsonResponse("get_comments_success.json")

        val request = GetCommentsRequest(session.clientKey, session.apiKey, session.tokenKey, articleId)
        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(400, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = CommentsManager.Factory(client).buildNative()
        val response = manager.getComments(request).blockingGet() as GetCommentsResponse.Error

        Assert.assertEquals(json, response.raw)
    }

    @Test
    fun testShouldParseAndReturnSuccessResultForSendCommentAction() {
        val parentId = 1
        val text = "sas"
        val articleId = 505240
        val json = getJsonResponse("send_comment_success.json")

        val url = "https://habr.com/api/v1/comments/$articleId?text=$text&parent_id=$parentId"
        val request = SendCommentRequest(session.clientKey, session.apiKey, session.tokenKey, articleId, text, parentId)

        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(200, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = CommentsManager.Factory(client).buildNative()
        val response = manager.sendComment(request).blockingGet() as SendCommentResponse.Success

        Assert.assertEquals(response.comment.message, text)
    }

    @Test
    fun testShouldParseAndReturnErrorResultForSendCommentAction() {
        val parentId = 1
        val text = "sas"
        val articleId = 505240
        val json = getJsonResponse("send_comment_success.json")

        val url = "https://habr.com/api/v1/comments/$articleId?text=$text&parent_id=$parentId"
        val request = SendCommentRequest(session.clientKey, session.apiKey, session.tokenKey, articleId, text, parentId)

        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(400, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = CommentsManager.Factory(client).buildNative()
        val response = manager.sendComment(request).blockingGet() as SendCommentResponse.Error

        Assert.assertEquals(json, response.raw)
    }

    @Test
    @Ignore("This test uses a real api call, so skip")
    fun voteUpCommentTest() {
        val commentId = 21395226
        val request = VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
        val manager = CommentsManager.Factory(OkHttpClient()).buildNative()
        val response = manager.voteUp(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore("This test uses a real api call, so skip")
    fun voteDownCommentTest() {
        val commentId = 21396134
        val request = VoteCommentRequest(session.clientKey, session.tokenKey, commentId)
        val manager = CommentsManager.Factory(OkHttpClient()).buildNative()
        val response = manager.voteDown(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore("This test uses a real api call, so skip")
    fun getCommentsTest() {
        val articleId = 442440
        val request = GetCommentsRequest(session.clientKey, session.apiKey, session.tokenKey, articleId)
        val manager = CommentsManager.Factory(OkHttpClient()).buildNative()
        val response = manager.getComments(request).blockingGet()
        println(response)
    }

}