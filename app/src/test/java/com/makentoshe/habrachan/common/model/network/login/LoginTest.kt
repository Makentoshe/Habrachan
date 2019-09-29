package com.makentoshe.habrachan.common.model.network.login

import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class LoginTest {

    private lateinit var login: Login
    private lateinit var api: HabrApi

    @Before
    fun init() {
        api = mockk {

        }
        login = Login(api, LoginConverterFactory())
    }

    @Test
    fun `should return error if response body is null`() {
        val mockedResponse = mockk<Response<Result.LoginResponse>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockk {
                every { string() } returns "{}"
            }
        }

        val mockedHabrApi = mockk<HabrApi> {
            every {
                loginThroughApi(allAny(), allAny(), allAny(), allAny(), allAny()).execute()
            } returns mockedResponse
        }

        val login = Login(mockedHabrApi, LoginConverterFactory())
        val response = login.execute(LoginRequest("", "", "", "", ""))

        verify(exactly = 1) { mockedResponse.isSuccessful }
        verify(exactly = 0) { mockedResponse.body() }
        verify(exactly = 1) { mockedResponse.errorBody() }
    }

    @Test
    fun `should return result on success`() {
        val mockedResponse = mockk<Response<Result.LoginResponse>> {
            every { isSuccessful } returns true
            every { body() } returns mockk {
                every { error } returns null
                every { success } returns mockk()
            }
        }

        val mockedHabrApi = mockk<HabrApi> {
            every {
                loginThroughApi(allAny(), allAny(), allAny(), allAny(), allAny()).execute()
            } returns mockedResponse
        }

        val login = Login(mockedHabrApi, LoginConverterFactory())
        val response = login.execute(LoginRequest("", "", "", "", ""))

        verify(exactly = 1) { mockedResponse.isSuccessful }
        verify(exactly = 1) { mockedResponse.body() }
        verify(exactly = 0) { mockedResponse.errorBody() }
    }
}