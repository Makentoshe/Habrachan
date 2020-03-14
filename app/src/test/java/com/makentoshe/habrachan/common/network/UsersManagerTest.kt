package com.makentoshe.habrachan.common.network

import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.common.network.request.UserRequest
import io.mockk.every
import io.mockk.mockk
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import java.io.File

class UsersManagerTest {

    private lateinit var manager: UsersManager
    private lateinit var client: OkHttpClient

    @Before
    fun before() {
        client = mockk<OkHttpClient>()
        manager = UsersManager.Builder(client).build()
    }

    @Test
    fun sas() {
        val json = getDataset("UsersManager.getMe.success.json")!!
        val mockResponse = buildMockedResponse(json)

        val request = UserRequest("client", "api", "token", "user_name")
        val response = manager.getUser(request).blockingGet()
        println(response)
    }

    private fun buildMockedResponse(json: String): Response {
        val mockBody = mockk<ResponseBody>()
        every { mockBody.string() } returns json
        every { mockBody.contentType() } returns "".toMediaType()
        val response = mockk<Response>()
        every { response.body } returns mockBody
        every { response.newBuilder() } returns Response.Builder()

        val mockCall = mockk<Call>()
        every { mockCall.execute() } returns response

        every { client.newCall(any()) } returns mockCall

        return response
    }

    private fun getDataset(name: String): String? {
        val s = File.separator
        val path = "${s}src${s}test${s}java${s}com${s}makentoshe${s}habrachan${s}dataset"
        val datasets = File(File("").absolutePath, path)
        val dataset = datasets.listFiles().find { it.name == name }
        return dataset?.readText()
    }

}