package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.login.LoginResponse
import okhttp3.ResponseBody

class LoginConverter {

    fun convertBody(body: ResponseBody): LoginResponse {
        return Gson().fromJson(body.string(), LoginResponse.Success::class.java)
    }

    fun convertError(body: ResponseBody): LoginResponse {
        val map = Gson().fromJson(body.string(), Map::class.java)
        val additional = (map["additional"] as Map<String, String>).values.joinToString(" ")
        return LoginResponse.Error(listOf(additional), (map["code"] as Double).toInt(), map["message"].toString())
    }
}
