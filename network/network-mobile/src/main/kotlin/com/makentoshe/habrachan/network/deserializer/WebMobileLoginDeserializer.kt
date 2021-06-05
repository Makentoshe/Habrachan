package com.makentoshe.habrachan.network.deserializer

import com.makentoshe.habrachan.entity.mobiles.login.LoginInitialState
import com.makentoshe.habrachan.network.exceptions.WebMobuleLoginDeserializeException
import com.makentoshe.habrachan.network.request.WebMobileLoginRequest
import com.makentoshe.habrachan.network.response.WebMobileLoginResponse
import org.jsoup.Jsoup

class WebMobileLoginDeserializer : MobileGsonDeserializer() {

    fun body(request: WebMobileLoginRequest, string: String): Result<WebMobileLoginResponse> = try {
        val document = Jsoup.parse(string)
        val initialStateData = document.body().select("script")[0].dataNodes()[0].wholeData
        val json = initialStateData.drop(27).dropLast(146)
        val initialState = gson.fromJson(json, LoginInitialState::class.java)
        println(initialState)
        Result.success(WebMobileLoginResponse(request, emptyList(), string))
    } catch (exception: Exception) {
        Result.failure(WebMobuleLoginDeserializeException(request, string, exception))
    }

}