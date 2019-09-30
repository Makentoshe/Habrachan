package com.makentoshe.habrachan.common.model.network.users

import com.makentoshe.habrachan.common.model.network.HabrApi
import io.mockk.mockk
import org.junit.Test
import retrofit2.Response
import com.makentoshe.habrachan.common.model.network.Result
import io.mockk.every
import io.mockk.verify

class GetUserByLoginTest {

    @Test
    fun `should return error if response body is null`() {
        val mockedResponse = mockk<Response<Result.GetUserByLoginResponse>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockk {
                every { string() } returns "{}"
            }
        }

        val mockedHabrApi = mockk<HabrApi> {
            every {
                getUserByLogin(allAny(), allAny(), allAny(), allAny()).execute()
            } returns mockedResponse
        }

        val getUserByLogin = GetUserByLogin(mockedHabrApi, GetUserByLoginConverterFactory())
        val response = getUserByLogin.execute(GetUserByLoginRequest("", "", "", ""))

        verify(exactly = 1) { mockedResponse.isSuccessful }
        verify(exactly = 0) { mockedResponse.body() }
        verify(exactly = 1) { mockedResponse.errorBody() }
    }

    @Test
    fun `should return result on success`() {
        val mockedResponse = mockk<Response<Result.GetUserByLoginResponse>> {
            every { isSuccessful } returns true
            every { body() } returns mockk {
                every { error } returns null
                every { success } returns mockk()
            }
        }

        val mockedHabrApi = mockk<HabrApi> {
            every {
                getUserByLogin(allAny(), allAny(), allAny(), allAny()).execute()
            } returns mockedResponse
        }

        val getUserByLogin = GetUserByLogin(mockedHabrApi, GetUserByLoginConverterFactory())
        val response = getUserByLogin.execute(GetUserByLoginRequest("", "", "", ""))

        verify(exactly = 1) { mockedResponse.isSuccessful }
        verify(exactly = 1) { mockedResponse.body() }
        verify(exactly = 0) { mockedResponse.errorBody() }
    }
}