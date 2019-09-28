package com.makentoshe.habrachan.common.model.network.flows

import com.makentoshe.habrachan.common.model.network.ErrorResult
import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class GetFlowsTest {

    private lateinit var api: HabrApi
    private lateinit var getFlows: GetFlows

    @Before
    fun init() {
        api = mockk()
        getFlows = GetFlows(api)
    }

    @Test
    fun `should return success result`() {
        val success = mockk<GetFlowsResult>()

        val result = mockk<Result.GetFlowsResult2>()
        every { result.success } returns success
        every { result.error } returns null

        val response = mockk<Response<Result.GetFlowsResult2>>()
        every { response.body() } returns result

        val call = mockk<Call<Result.GetFlowsResult2>>()
        every { call.execute() } returns response

        every { api.getFlows(any(), any(), any()) } returns call

        getFlows.execute(GetFlowsRequest("", "")).also {
            assertNotNull(it.success)
            assertNull(it.error)
        }
    }

    @Test
    fun `should return error result`() {
        val message = "Message"
        val code = 444

        val response = mockk<Response<Result.GetFlowsResult2>>()
        every { response.body() } returns null
        every { response.errorBody()?.string() } returns message
        every { response.code() } returns code

        val call = mockk<Call<Result.GetFlowsResult2>>()
        every { call.execute() } returns response

        every { api.getFlows(any(), any(), any()) } returns call

        getFlows.execute(GetFlowsRequest("", "")).also {
            assertNull(it.success)
            assertNotNull(it.error)
        }
    }
}