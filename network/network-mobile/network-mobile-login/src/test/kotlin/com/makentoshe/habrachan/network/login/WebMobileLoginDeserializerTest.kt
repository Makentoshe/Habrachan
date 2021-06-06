package com.makentoshe.habrachan.network.login

import org.junit.Ignore
import org.junit.Test

class WebMobileLoginDeserializerTest : UnitTest() {

    private val deserializer = WebMobileLoginDeserializer()

    @Test
    @Ignore
    fun testShould() {
        val string = getResourceString("login_success.html")
        val result = deserializer.deserializeLoginScreenPassedResponse(string)
        println(result)
    }
}