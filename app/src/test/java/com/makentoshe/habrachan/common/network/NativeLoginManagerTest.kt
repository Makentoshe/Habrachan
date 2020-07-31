package com.makentoshe.habrachan.common.network

import com.makentoshe.habrachan.*
import com.makentoshe.habrachan.common.network.manager.LoginManager
import com.makentoshe.habrachan.common.network.request.LoginRequest
import com.makentoshe.habrachan.common.network.request.OAuthRequest
import com.makentoshe.habrachan.common.network.response.LoginResponse
import com.makentoshe.habrachan.common.network.response.OAuthResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class NativeLoginManagerTest : BaseTest() {

    @Test
    fun testShouldParseAndReturnSuccessResultForLoginAction() {
        val json = getJsonResponse("base_login_success.json")
        val url = "https://habr.com/auth/o/access-token"
        val request = LoginRequest(session.clientKey, session.tokenKey, "email", "password")
        val client = OkHttpClient.Builder()
            .addInterceptor(JsonResponseInterceptor(200, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = LoginManager.Builder(client).build()
        val response = manager.login(request).blockingGet() as LoginResponse.Success

        assertEquals("access_token", response.accessToken)
        assertEquals("2020-04-07T14:44:32+03:00", response.serverTime)
    }

    @Test
    fun testShouldParseAndReturnErrorResultForLoginAction1() {
        val json = getJsonResponse("base_login_error_1.json")
        val url = "https://habr.com/auth/o/access-token"
        val request = LoginRequest(session.clientKey, session.tokenKey, "email", "password")
        val client = OkHttpClient.Builder()
            .addInterceptor(JsonResponseInterceptor(400, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = LoginManager.Builder(client).build()
        val response = manager.login(request).blockingGet() as LoginResponse.Error

        assertEquals(400, response.code)
        assertEquals("Habr Account error", response.message)
        assertEquals(1, response.additional.size)
        assertEquals(json, response.additional[0])
    }

    @Test
    fun testShouldParseAndReturnErrorResultForLoginAction2() {
        val json = getJsonResponse("base_login_error_2.json")
        val url = "https://habr.com/auth/o/access-token"
        val request = LoginRequest(session.clientKey, session.tokenKey, "email", "password")
        val client = OkHttpClient.Builder()
            .addInterceptor(JsonResponseInterceptor(400, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = LoginManager.Builder(client).build()
        val response = manager.login(request).blockingGet() as LoginResponse.Error

        assertEquals(400, response.code)
        assertEquals("Bad request", response.message)
        assertEquals(1, response.additional.size)
        assertEquals(json, response.additional[0])
    }

    @Test
    fun testShouldReturnSuccessResultForGithubOauthAction() {
        val url =
            "https://habr.com/auth/o/social-login?client_id=85cab69095196f3.89453480&social_type=github&response_type=token&redirect_uri=http%3A%2F%2Fcleverpumpkin.ru"
        val request = OAuthRequest(session.clientKey, "github", "https://github.com")
        val responseRequest = Request.Builder().url("http://test.okhttp.url").build()

        val client = OkHttpClient.Builder()
            .addInterceptor(UrlInterceptor(url))
            .addInterceptor(ResponseRequestInterceptor(responseRequest))
            .build()
        val manager = LoginManager.Builder(client).build()
        val response = manager.oauth(request).blockingGet() as OAuthResponse.Interim

        assertEquals(request, response.request)
        assertEquals(responseRequest.url.toString(), response.url)
        assertEquals(0, response.cookies.size)
    }

    @Test
    @Ignore("Test uses real api")
    fun loginTest() {
        val request = LoginRequest(session.clientKey, session.apiKey, BuildConfig.LOGIN, BuildConfig.PASSWORD)
        val manager = LoginManager.Builder(OkHttpClient()).build()
        val response = manager.login(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore("Test uses real api")
    fun githubOauthTest() {
        val request = OAuthRequest(session.clientKey, "github", "https://github.com")
        val manager = LoginManager.Builder(OkHttpClient.Builder().followRedirects(false).build()).build()
        val response = manager.oauth(request).blockingGet()
        println(response)
    }
}