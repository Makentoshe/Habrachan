package com.makentoshe.habrachan.network

import UnitTest
import com.makentoshe.habrachan.functional.getOrThrow
import io.mockk.every
import io.mockk.mockk
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetContentManagerTest : UnitTest() {

    private val okHttpClient = mockk<OkHttpClient>()

    @Test
    fun testShouldCheckFailureOnBadUrl() {
        val manager = GetContentManager(okHttpClient)
        val request = manager.request(userSession, "url")
        val response = manager.content(request).exceptionOrNull() as GetContentException

        assertEquals(userSession, request.userSession)
        assertTrue(response.cause is IllegalArgumentException)
    }

    @Test
    fun testShouldCheckSuccessResponse() {
        val string = "Just another string created for the testing purposes"
        every { okHttpClient.newCall(any()) } returns mockedCallResponse {
            mockedResponse(true, 200, "") {
                mockedResponseBody(string)
            }
        }

        val manager = GetContentManager(okHttpClient)
        val request = manager.request(userSession, "https://habr.com/")
        val response = manager.content(request).getOrThrow()

        assertEquals(string, response.bytes.decodeToString())
        assertEquals(userSession, request.userSession)
    }

    @Test
    fun testShouldCheckFailureResponse() {
        val string = "Just another string created for the testing purposes"
        every { okHttpClient.newCall(any()) } returns mockedCallResponse {
            mockedResponse(false, 404, "Some error message") {
                mockedResponseBody(string)
            }
        }

        val manager = GetContentManager(okHttpClient)
        val request = manager.request(userSession, "https://habr.com/")
        val response = manager.content(request).exceptionOrNull() as GetContentException

        assertEquals(string, response.message)
        assertEquals(userSession, request.userSession)
    }

    private fun mockedResponseBody(string: String): ResponseBody {
        val mockResponseBody = mockk<ResponseBody>()
        every { mockResponseBody.string() } returns string
        every { mockResponseBody.bytes() } returns string.encodeToByteArray()
        return mockResponseBody
    }

    private fun mockedResponse(
        isSuccessful: Boolean = true,
        code: Int = 200,
        message: String = "Mocked message",
        responseBodyScope: () -> ResponseBody
    ): Response {
        val mockResponse = mockk<Response>()
        every { mockResponse.isSuccessful } returns isSuccessful
        every { mockResponse.code } returns code
        every { mockResponse.message } returns message
        every { mockResponse.body } returns responseBodyScope()
        return mockResponse
    }

    private fun mockedCallResponse(mockResponseScope: () -> Response): Call {
        val mockCallResponseBody = mockk<Call>()
        every { mockCallResponseBody.execute() } returns mockResponseScope()
        return mockCallResponseBody
    }
}