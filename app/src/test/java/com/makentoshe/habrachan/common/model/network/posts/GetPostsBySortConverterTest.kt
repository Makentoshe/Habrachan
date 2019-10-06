package com.makentoshe.habrachan.common.model.network.posts

import com.makentoshe.habrachan.common.model.network.posts.bysort.GetPostsBySortConverter
import com.makentoshe.habrachan.resources.testResourcesDirectory
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class GetPostsBySortConverterTest {

    private val converter = GetPostsBySortConverter()

    @Test
    fun `should parse GetPostsBySortSuccess`() {
        val successJson = File(testResourcesDirectory, "GetPostsBySortSuccess.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns successJson.readText()

        converter.convert(responseBody).also {
            assertNull(it.error)
            assertNotNull(it.success)
            val data = it.success!!.data
            assertEquals(50, data.pages)
            assertEquals(20, data.data.size)
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
        val errorJson = File(testResourcesDirectory, "GetPostsBySortError.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns errorJson.readText()

        converter.convert(responseBody).also {
            assertNull(it.success)
            assertNotNull(it.error)
            assertEquals(400, it.error!!.code)
            assertEquals("invalid page", it.error!!.message)
            assertTrue(it.error!!.additional.isEmpty())
        }
    }
}