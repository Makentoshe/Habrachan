package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.login.LoginResponse
import okhttp3.ResponseBody

class LoginConverter {

    fun convertBody(body: ResponseBody): LoginResponse {
        return Gson().fromJson(body.string(), LoginResponse.Success::class.java)
    }

    fun convertError(body: ResponseBody): LoginResponse {
        val json = body.string()
        val map = Gson().fromJson(json, Map::class.java)
//        val additional = (map["additional"] as List<String>).joinToString(" ")
        return LoginResponse.Error(listOf(json), (map["code"] as Double).toInt(), map["message"].toString())
    }
}
