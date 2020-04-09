package com.makentoshe.habrachan.common.network

import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.ResponseInterceptor
import com.makentoshe.habrachan.UrlInterceptor
import com.makentoshe.habrachan.common.entity.user.UserResponse
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.common.network.request.MeRequest
import com.makentoshe.habrachan.common.network.request.UserRequest
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class UsersManagerTest : BaseTest() {

    @Test
    fun testShouldParseAndReturnSuccessResultForGetMeAction() {
        val json = getJsonResponse("get_me_success.json")
        val url = "https://habr.com/api/v1/users/me"

        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(200, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = UsersManager.Builder(client).build()
        val request = MeRequest(session.clientKey, session.tokenKey)
        val response = manager.getMe(request).blockingGet() as UserResponse.Success

        assertEquals("Makentoshe", response.user.login)
    }

    @Test
    fun testShouldParseAndReturnErrorResultForGetMeAction() {
        val json = getJsonResponse("get_me_error.json")
        val url = "https://habr.com/api/v1/users/me"

        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(401, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = UsersManager.Builder(client).build()
        val request = MeRequest(session.clientKey, session.tokenKey)
        val response = manager.getMe(request).blockingGet() as UserResponse.Error

        assertEquals(401, response.code)
        assertEquals("Authorization required", response.message)
    }

    @Test
    fun testShouldParseAndReturnSuccessResultForGetUserAction() {
        val json = getJsonResponse("get_user_success.json")
        val url = "https://habr.com/api/v1/users/missingdays"

        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(200, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = UsersManager.Builder(client).build()
        val request = UserRequest(session.clientKey, session.tokenKey, "missingdays")
        val response = manager.getUser(request).blockingGet() as UserResponse.Success

        assertEquals("missingdays", response.user.login)
    }

    @Test
    fun testShouldParseAndReturnErrorResultForGetUserAction() {
        val json = getJsonResponse("get_user_error.json")
        val url = "https://habr.com/api/v1/users/missingdays"

        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(401, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = UsersManager.Builder(client).build()
        val request = UserRequest(session.clientKey, session.tokenKey, "missingdays")
        val response = manager.getUser(request).blockingGet() as UserResponse.Error

        assertEquals(401, response.code)
        assertEquals("Authorization required", response.message)
    }

    @Test
    @Ignore("Test uses real api")
    fun getMe() {
        val request = MeRequest(session.clientKey, session.tokenKey)
        val manager = UsersManager.Builder(OkHttpClient()).build()
        val response = manager.getMe(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore("Test uses real api")
    fun getCustomUserByName() {
        val request = UserRequest("sas", session.tokenKey, "missingdays")
        val manager = UsersManager.Builder(OkHttpClient()).build()
        val response = manager.getUser(request).blockingGet()
        println(response)
    }
}