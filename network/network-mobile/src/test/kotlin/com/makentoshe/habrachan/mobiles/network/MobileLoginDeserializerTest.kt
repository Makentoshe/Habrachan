package com.makentoshe.habrachan.mobiles.network

import com.makentoshe.habrachan.network.deserializer.MobileLoginDeserializer
import com.makentoshe.habrachan.network.exceptions.MobileLoginResponseException
import com.makentoshe.habrachan.network.request.MobileLoginRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MobileLoginDeserializerTest : UnitTest() {

    private val deserializer = MobileLoginDeserializer()

    @Test
    fun shouldDeserializeLoginFailureWithMissingFields() {
        val string = getResourceString("login_failure_1.js")
        val request = MobileLoginRequest(userSession, "", "")
        val exception = deserializer.error(request, string).exceptionOrNull() as MobileLoginResponseException

        assertEquals(string, exception.raw)
        assertEquals(request, exception.request)
        assertEquals("Введите пароль", exception.password)
        assertEquals("Введите корректный e-mail", exception.email)
        assertNull(exception.other)
    }

    @Test
    fun shouldDeserializeLoginFailureWithAccountError() {
        val string = getResourceString("login_failure_2.js")
        val request = MobileLoginRequest(userSession, "", "")
        val exception = deserializer.error(request, string).exceptionOrNull() as MobileLoginResponseException

        assertEquals(string, exception.raw)
        assertEquals(request, exception.request)
        assertEquals("There&apos;s no user with such email and password", exception.other)
        assertNull(exception.email)
        assertNull(exception.password)
    }
}
