package com.makentoshe.habrachan.common.model.network.login

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.network.ErrorResult
import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class LoginConverterFactory : Converter.Factory() {

    val converter = Converter<ResponseBody, Result.LoginResponse> {
        val json = it.string()
        val success = Gson().fromJson(json, LoginResult::class.java)
        if (success.accessToken == null) {
            val error = Gson().fromJson(json, ErrorResult::class.java)
            return@Converter Result.LoginResponse(success = null, error = error)
        } else {
            return@Converter Result.LoginResponse(success = success, error = null)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.LoginResponse>? {
        return if (type == Result.LoginResponse::class.java) converter else null
    }
}