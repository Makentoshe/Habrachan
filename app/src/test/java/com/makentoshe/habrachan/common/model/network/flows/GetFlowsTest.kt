package com.makentoshe.habrachan.common.model.network.flows

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

class GetFlowsTest {

    @Test
    fun `should return error if response body is null`() {
        val mockedResponse = mockk<Response<Result.GetFlowsResponse>> {
            every { isSuccessful } returns false
            every { errorBody() } returns mockk {
                every { string() } returns "{}"
            }
        }

        val mockedHabrApi = mockk<HabrApi> {
            every {
                getFlows(allAny(), allAny(), allAny()).execute()
            } returns mockedResponse
        }

        val login = GetFlows(mockedHabrApi, GetFlowsConverterFactory())
        val response = login.execute(GetFlowsRequest("", "", ""))

        verify(exactly = 1) { mockedResponse.isSuccessful }
        verify(exactly = 0) { mockedResponse.body() }
        verify(exactly = 1) { mockedResponse.errorBody() }
    }

    @Test
    fun `should return result on success`() {
        val mockedResponse = mockk<Response<Result.GetFlowsResponse>> {
            every { isSuccessful } returns true
            every { body() } returns mockk {
                every { error } returns null
                every { success } returns mockk()
            }
        }

        val mockedHabrApi = mockk<HabrApi> {
            every {
                getFlows(allAny(), allAny(), allAny()).execute()
            } returns mockedResponse
        }

        val login = GetFlows(mockedHabrApi, GetFlowsConverterFactory())
        val response = login.execute(GetFlowsRequest("", "", ""))

        verify(exactly = 1) { mockedResponse.isSuccessful }
        verify(exactly = 1) { mockedResponse.body() }
        verify(exactly = 0) { mockedResponse.errorBody() }
    }
}