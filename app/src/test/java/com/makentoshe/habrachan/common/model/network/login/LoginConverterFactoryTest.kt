package com.makentoshe.habrachan.common.model.network.login

import com.makentoshe.habrachan.resources.testResourcesDirectory
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Converter
import java.io.File

class LoginConverterFactoryTest {

    private val factory = LoginConverterFactory()
    private lateinit var converter: Converter<ResponseBody, LoginResult>

    @Before
    fun init() {
        converter = factory.responseBodyConverter(LoginResult::class.java, arrayOf(), mockk())!!
    }

    @Test
    fun `converter should be null for incompatible type`() {
        assertNull(factory.responseBodyConverter(Any::class.java, arrayOf(), mockk()))
    }

    @Test
    fun `should parse success result`() {
        val successJson = File(testResourcesDirectory, "LoginSuccess.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns successJson.readText()

        converter.convert(responseBody).also {
            assertEquals("access_token_for_user", it.accessToken)
            assertEquals("2019-09-27T22:25:48+03:00", it.serverTime)
            assertNull(it.code)
            assertNull(it.message)
            assertNull(it.additional)
        }
    }

    @Test
    fun `should parse error result`() {
        val errorJson = File(testResourcesDirectory, "LoginError.json")

        val responseBody = mockk<ResponseBody>()
        every { responseBody.string() } returns errorJson.readText()

        converter.convert(responseBody).also {
            assertEquals(400, it.code)
            assertEquals("Habr Account error", it.message)
            assertEquals("Пользователь с такой электронной почтой или паролем не найден", it.additional!!.errors)
            assertNull(it.serverTime)
            assertNull(it.accessToken)
        }
    }
}