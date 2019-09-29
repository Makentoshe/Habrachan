package com.makentoshe.habrachan.common.model.network.hubs

import com.makentoshe.habrachan.common.model.network.Result
import com.makentoshe.habrachan.resources.testResourcesDirectory
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Converter
import java.io.File

class GetHubsConverterFactoryTest {

    private val factory = GetHubsConverterFactory()
    private lateinit var converter: Converter<ResponseBody, Result.GetHubsResponse>

    @Before
    fun init() {
        converter = factory.responseBodyConverter(Result.GetHubsResponse::class.java, arrayOf(), mockk())!!
    }

    @Test
    fun `should parse success result`() {
        val successJson = File(testResourcesDirectory, "GetHubsSuccess.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns successJson.readText()

        converter.convert(responseBody).also {
            assertNotNull(it.success)
            assertNull(it.error)
        }
    }

    @Test
    fun `should parse api error result`() {
        val errorJson = File(testResourcesDirectory, "ErrorResult.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns errorJson.readText()

        converter.convert(responseBody).also {
            assertNotNull(it.error)
            assertNull(it.success)
        }
    }
}