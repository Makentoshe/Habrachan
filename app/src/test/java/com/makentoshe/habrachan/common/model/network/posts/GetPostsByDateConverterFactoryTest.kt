package com.makentoshe.habrachan.common.model.network.posts

import com.makentoshe.habrachan.common.model.network.posts.bydate.GetPostsByDateConverterFactory
import okhttp3.ResponseBody
import retrofit2.Converter
import com.makentoshe.habrachan.common.model.network.Result
import com.makentoshe.habrachan.resources.testResourcesDirectory
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.io.File

class GetPostsByDateConverterFactoryTest {

    private val factory = GetPostsByDateConverterFactory()
    private lateinit var converter: Converter<ResponseBody, Result.GetPostsByDateResponse>

    @Before
    fun init() {
        converter = factory.responseBodyConverter(Result.GetPostsByDateResponse::class.java, arrayOf(), mockk())!!
    }

    @Test
    fun `converter should be null for incompatible type`() {
        assertNull(factory.responseBodyConverter(Any::class.java, arrayOf(), mockk()))
    }

    @Test
    fun `should parse GetPostsBySortSuccess`() {
        val successJson = File(testResourcesDirectory, "GetPostsByDateSuccess.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns successJson.readText()

        converter.convert(responseBody).also {
            assertNotNull(it.success)
            assertNull(it.error)
        }
    }

    @Test
    fun `should parse ErrorResult`() {
        val errorJson = File(testResourcesDirectory, "ErrorResult.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns errorJson.readText()

        converter.convert(responseBody).also {
            assertNotNull(it.error)
            assertNull(it.success)
        }
    }

    @Test
    fun `should parse GetPostsBySortError`() {
        val errorJson = File(testResourcesDirectory, "GetPostsByDateError.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns errorJson.readText()

        converter.convert(responseBody).also {
            assertNotNull(it.error)
            assertNull(it.success)
        }
    }
}