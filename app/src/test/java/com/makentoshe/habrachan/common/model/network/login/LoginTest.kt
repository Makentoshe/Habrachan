package com.makentoshe.habrachan.common.model.network.login

import com.makentoshe.habrachan.common.model.network.CookieStorage
import com.makentoshe.habrachan.common.model.network.HabrApi
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class LoginTest {

    private lateinit var login: Login
    private lateinit var api: HabrApi
    private lateinit var cookieStorage: CookieStorage

    @Before
    fun init() {
        cookieStorage = mockk()
        api = mockk()
        login = Login(api, cookieStorage)
    }

    @Test
    fun `should return error if response body is null`() {
        val request = LoginRequest("", "", "", "", "")
        val message = "Message"
        val code = 12345

        val response = mockk<Response<LoginResult>>()
        every { response.body() } returns null
        every { response.message() } returns message
        every { response.code() } returns code

        val call = mockk<Call<LoginResult>>()
        every { call.execute() } returns response

        every { api.loginThroughApi(any(), any(), any(), any(), any(), any(), any()) } returns call

        login.execute(request).also { result ->
            assertFalse(result.success)
            assertEquals(code, result.code)
            assertEquals(message, result.message)
        }
    }

    @Test
    fun `should return result on success`() {
        val request = LoginRequest("", "", "", "", "")
        val result = mockk<LoginResult>()
        val copiedResult = LoginResult(true)
        every { result.copy(any(), any(), any(), any(), any(), any()) } returns copiedResult

        val response = mockk<Response<LoginResult>>()
        every { response.body() } returns result

        val call = mockk<Call<LoginResult>>()
        every { call.execute() } returns response

        every { api.loginThroughApi(any(), any(), any(), any(), any(), any(), any()) } returns call

        login.execute(request).also {
            assertEquals(copiedResult, it)
        }
    }
}