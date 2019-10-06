package com.makentoshe.habrachan.common.model.network.votepost

import com.makentoshe.habrachan.resources.testResourcesDirectory
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertNotNull
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class VotePostConverterTest {


    private val converter = VotePostConverter()

    @Test
    fun `should parse GetPostsBySortSuccess`() {
        val successJson = File(testResourcesDirectory, "VotePostSuccess.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns successJson.readText()

        converter.convert(responseBody).also {
            assertNull(it.error)
            assertNotNull(it.success)
            assertEquals(1, it.success!!.score)
            assertEquals("2019-09-29T14:21:30+03:00", it.success!!.serverTime)
        }
    }

    @Test
    fun `should parse ErrorResult`() {
        val errorJson = File(testResourcesDirectory, "ErrorResult.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns errorJson.readText()

        converter.convert(responseBody).also {
            assertNull(it.success)
            it.error.also(::assertNotNull)!!.let { errorResult ->
                assertEquals(403, errorResult.code)
                assertEquals("Authorization required", errorResult.message)
            }
        }
    }

    @Test
    fun `should parse GetPostsBySortError`() {
        val errorJson = File(testResourcesDirectory, "VotePostError.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns errorJson.readText()

        converter.convert(responseBody).also {
            assertNull(it.success)
            assertNotNull(it.error)
            assertEquals(400, it.error!!.code)
            assertEquals("Incorrect parameters", it.error!!.message)
            assertTrue(it.error!!.additional.isNotEmpty())
        }
    }
}