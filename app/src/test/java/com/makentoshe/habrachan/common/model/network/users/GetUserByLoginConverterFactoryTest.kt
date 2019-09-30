package com.makentoshe.habrachan.common.model.network.users

import com.makentoshe.habrachan.common.model.network.Result
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
    private lateinit var converter: Converter<ResponseBody, Result.GetUserByLoginResponse>

    @Before
    fun init() {
        converter = factory.responseBodyConverter(Result.GetUserByLoginResponse::class.java, arrayOf(), mockk())!!
    }

    @Test
    fun `converter should be null for incompatible type`() {
        assertNull(factory.responseBodyConverter(Any::class.java, arrayOf(), mockk()))
    }

    @Test
    fun `should parse success result`() {
        val successJson = File(testResourcesDirectory, "GetUserByLoginSuccess.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns successJson.readText()

        converter.convert(responseBody).also {
            assertNotNull(it.success)
            assertNull(it.error)
        }
    }

    @Test
    fun `should parse error result`() {
        val errorJson = File(testResourcesDirectory, "GetUserByLoginError.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns errorJson.readText()

        converter.convert(responseBody).also {
            assertNotNull(it.error)
            assertNull(it.success)
        }
    }
}
