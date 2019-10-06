package com.makentoshe.habrachan.common.model.network.posts

import com.makentoshe.habrachan.resources.testResourcesDirectory
import com.makentoshe.habrachan.common.model.network.Result
import com.makentoshe.habrachan.common.model.network.posts.byquery.GetPostsByQueryConverterFactory
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Converter
import java.io.File

class PostBySearchConverterFactoryTest {

    private val factory = GetPostsByQueryConverterFactory()
    private lateinit var converter: Converter<ResponseBody, Result.GetPostsByQueryResponse>

    @Before
    fun init() {
        converter = factory.responseBodyConverter(Result.GetPostsByQueryResponse::class.java, arrayOf(), mockk())!!
    }

    @Test
    fun `converter should be null for incompatible type`() {
        assertNull(factory.responseBodyConverter(Any::class.java, arrayOf(), mockk()))
    }

    @Test
    fun `should parse success result`() {
        val successJson = File(testResourcesDirectory, "GetPostsByQuerySuccess.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns successJson.readText()

        converter.convert(responseBody).also {
            assertNotNull(it.success)
            assertNull(it.error)
        }
    }

    @Test
    fun `should parse error result`() {
        val errorJson = File(testResourcesDirectory, "GetPostsByQueryError.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns errorJson.readText()

        converter.convert(responseBody).also {
            assertNotNull(it.error)
            assertNull(it.success)
        }
    }
}
