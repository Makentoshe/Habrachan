package com.makentoshe.habrachan.common.model.network.flows

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

class GetFlowsConverterFactoryTest {

    private val factory = GetFlowsConverterFactory()
    private lateinit var converter: Converter<ResponseBody, Result.GetFlowsResult2>

    @Before
    fun init() {
        converter = factory.responseBodyConverter(Result.GetFlowsResult2::class.java, arrayOf(), mockk())!!
    }

    @Test
    fun `should parse success result`() {
        val successJson = File(testResourcesDirectory, "GetFlowsSuccess.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns successJson.readText()

        converter.convert(responseBody).also {
            assertNotNull(it.success)
            assertNull(it.error)
        }
    }

    @Test
    fun `should parse error result`() {
        val errorJson = File(testResourcesDirectory, "ErrorResult.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns errorJson.readText()

        converter.convert(responseBody).also {
            assertNotNull(it.error)
            assertNull(it.success)
        }
    }
}