package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.makentoshe.habrachan.common.entity.Badges
import com.makentoshe.habrachan.common.network.response.UserResponse
import okhttp3.ResponseBody

class UsersConverter {

    fun convertBody(body: ResponseBody): UserResponse.Success {
        val gson = GsonBuilder().registerTypeAdapter(Badges::class.java, BadgesDeserializer()).create()
        return gson.fromJson(body.string(), UserResponse.Success::class.java)
    }

    fun convertError(body: ResponseBody): UserResponse.Error {
        return Gson().fromJson(body.string(), UserResponse.Error::class.java)
    }
}