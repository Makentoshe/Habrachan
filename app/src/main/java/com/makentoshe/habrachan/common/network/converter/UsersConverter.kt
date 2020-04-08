package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.user.UserResponse
import okhttp3.ResponseBody

class UsersConverter {

    fun convertBody(body: ResponseBody): UserResponse.Success {
        return Gson().fromJson(body.string(), UserResponse.Success::class.java)
    }

    fun convertError(body: ResponseBody): UserResponse.Error {
        return Gson().fromJson(body.string(), UserResponse.Error::class.java)
    }
}