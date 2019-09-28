package com.makentoshe.habrachan.common.model.network.users

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetUserByLoginConverterFactory: Converter.Factory() {

    private val converter by lazy {
        Converter<ResponseBody, GetUserByLoginResult> {
            Gson()
                .fromJson(it.string(), GetUserByLoginResult::class.java)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, GetUserByLoginResult>? {
        return if (type == GetUserByLoginResult::class.java) converter else null
    }
}