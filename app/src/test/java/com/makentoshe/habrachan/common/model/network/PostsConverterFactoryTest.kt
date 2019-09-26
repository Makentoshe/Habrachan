package com.makentoshe.habrachan.common.model.network

import com.makentoshe.habrachan.common.model.network.posts.GetPostsResult
import com.makentoshe.habrachan.common.model.network.posts.PostsConverterFactory
import com.makentoshe.habrachan.resources.testResourcesDirectory
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Converter
import java.io.File

class PostsConverterFactoryTest {

    private val factory = PostsConverterFactory()
    private var converter: Converter<ResponseBody, GetPostsResult>? = null

    @Before
    fun init() {
        converter = factory.responseBodyConverter(mockk(), arrayOf(), mockk())
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
