package com.makentoshe.habrachan.common.model.network.votepost

import com.makentoshe.habrachan.common.model.network.Result
import com.makentoshe.habrachan.common.model.network.flows.GetFlowsConverterFactory
import com.makentoshe.habrachan.resources.testResourcesDirectory
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Converter
import java.io.File

class VotePostConverterFactoryTest {

    private val factory = VotePostConverterFactory()
    private lateinit var converter: Converter<ResponseBody, Result.VotePostResponse>

    @Before
    fun init() {
        converter = factory.responseBodyConverter(Result.VotePostResponse::class.java, arrayOf(), mockk())!!
    }

    @Test
    fun `should parse success result`() {
        val successJson = File(testResourcesDirectory, "VotePostSuccess.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns successJson.readText()

        converter.convert(responseBody).also {
            assertNull(it.error)
            assertNotNull(it.success)
            assertEquals(1, it.success!!.score)
            assertEquals("2019-09-29T14:21:30+03:00", it.success!!.serverTime)
            assertTrue(it.success!!.success)
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

    @Test
    fun `should parse error result`() {
        val errorJson = File(testResourcesDirectory, "VotePostError.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns errorJson.readText()

        converter.convert(responseBody).also {
            assertNotNull(it.error)
            assertNull(it.success)
        }
    }
}