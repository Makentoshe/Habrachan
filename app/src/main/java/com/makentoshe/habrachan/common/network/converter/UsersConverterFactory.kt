package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.user.UserResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class UsersConverterFactory : Converter.Factory() {

    private val converter = Converter<ResponseBody, UserResponse> {
        Gson().fromJson(it.string(), UserResponse::class.java)
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, UserResponse>? {
        return if (type == UserResponse::class.java) converter else null
    }
}
