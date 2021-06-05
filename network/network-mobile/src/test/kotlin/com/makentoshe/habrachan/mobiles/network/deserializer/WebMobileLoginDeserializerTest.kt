package com.makentoshe.habrachan.mobiles.network.deserializer

import com.makentoshe.habrachan.mobiles.network.UnitTest
import com.makentoshe.habrachan.network.deserializer.WebMobileLoginDeserializer
import com.makentoshe.habrachan.network.request.WebMobileLoginRequest
import org.junit.Test

class WebMobileLoginDeserializerTest : UnitTest() {

    private val deserializer = WebMobileLoginDeserializer()

    @Test
    fun testShould() {
        val string = getResourceString("login_success.html")
        val result = deserializer.body(WebMobileLoginRequest(userSession, { it }), string)
        println(result)
    }
}