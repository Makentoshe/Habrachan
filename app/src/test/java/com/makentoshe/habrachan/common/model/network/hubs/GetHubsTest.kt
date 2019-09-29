package com.makentoshe.habrachan.common.model.network.hubs

import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class GetHubsTest {

    private lateinit var api: HabrApi
    private lateinit var getHubs: GetHubs

    @Before
    fun init() {
        api = mockk()
        getHubs = GetHubs(api, GetHubsConverterFactory())
    }

    @Test
    fun `should return success result`() {
        val success = mockk<GetHubsResult>()

        val result = mockk<Result.GetHubsResponse>()
        every { result.success } returns success
        every { result.error } returns null

        val response = mockk<Response<Result.GetHubsResponse>>()
        every { response.body() } returns result
        every { response.isSuccessful } returns true

        val call = mockk<Call<Result.GetHubsResponse>>()
        every { call.execute() } returns response

        every { api.getHubs(any(), any(), any(), any(), any()) } returns call

        getHubs.execute(GetHubsRequest("", "", "", "", 1)).also {
            assertNotNull(it.success)
            assertNull(it.error)
        }
    }

    @Test
    fun `should return api error result`() {
        val errorBody = mockk<ResponseBody>()
        every { errorBody.string() } returns "{}"

        val response = mockk<Response<Result.GetHubsResponse>>()
        every { response.body() } returns null
        every { response.errorBody() } returns errorBody
        every { response.isSuccessful } returns false

        val call = mockk<Call<Result.GetHubsResponse>>()
        every { call.execute() } returns response

        every { api.getHubs(any(), any(), any(), any(), any()) } returns call

        getHubs.execute(GetHubsRequest("", "", "", "", 1)).also {
            assertNull(it.success)
            assertNotNull(it.error)
        }
    }
}