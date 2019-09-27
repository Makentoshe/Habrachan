package com.makentoshe.habrachan.common.model.network.posts

import com.makentoshe.habrachan.resources.testResourcesDirectory
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Converter
import java.io.File

class PostBySearchConverterFactoryTest {

    private val factory = PostsBySearchConverterFactory()
    private var converter: Converter<ResponseBody, GetPostsBySearchResult>? = null

    @Before
    fun init() {
        converter = factory.responseBodyConverter(GetPostsBySearchResult::class.java, arrayOf(), mockk())
    }

    @Test
    fun `should parse success result`() {
        val successJson = File(testResourcesDirectory, "GetPostsBySearchSuccess.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns successJson.readText()

        converter!!.convert(responseBody).also {
            assertTrue(it.success)
            assertEquals(50, it.data.pages)
            assertEquals(20, it.data.articles!!.size)
        }
    }

    @Test
    fun `should parse error result`() {
        val errorJson = File(testResourcesDirectory, "GetPostsBySearchError.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns errorJson.readText()

        converter!!.convert(responseBody).also {
            assertFalse(it.success)
            assertEquals(400, it.data.code)
            assertNull(it.data.data)
            assertEquals("Сожалеем, поиск в публикациях не дал результатов", it.data.message)
        }
    }
}