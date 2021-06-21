package com.makentoshe.habrachan.network.login

import com.makentoshe.habrachan.entity.mobiles.login.LoginInitialState
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.deserializer.MobileGsonDeserializer
import org.jsoup.Jsoup

internal class WebMobileLoginDeserializer : MobileGsonDeserializer() {

    fun deserializeLoginScreenPassedResponse(string: String): Result<LoginInitialState> = try {
        val document = Jsoup.parse(string)
        val initialStateData = document.body().select("script")[0].dataNodes()[0].wholeData
        val json = initialStateData.trimIndent().drop(25).dropLast(122)
        Result.success(gson.fromJson(json, LoginInitialState::class.java))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

}