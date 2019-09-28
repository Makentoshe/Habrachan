package com.makentoshe.habrachan.common.model.network.users

import com.makentoshe.habrachan.resources.testResourcesDirectory
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Converter
import java.io.File

class GetUserByLoginConverterFactoryTest {

    private val factory = GetUserByLoginConverterFactory()
    private lateinit var converter: Converter<ResponseBody, GetUserByLoginResult>

    @Before
    fun init() {
        converter = factory.responseBodyConverter(GetUserByLoginResult::class.java, arrayOf(), mockk())!!
    }

    @Test
    fun `converter should be null for incompatible type`() {
        assertNull(factory.responseBodyConverter(Any::class.java, arrayOf(), mockk()))
    }

    @Test
    fun `should parse success result`() {
        val successJson = File(testResourcesDirectory, "GetUsersBySearchSuccess.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns successJson.readText()

        converter.convert(responseBody).also {
            assertTrue(it.success)
        }
    }

    @Test
    fun `should parse error result`() {
        val errorJson = File(testResourcesDirectory, "GetPostsBySearchError.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns errorJson.readText()

        converter.convert(responseBody).also {
            assertFalse(it.success)
        }
    }
}
