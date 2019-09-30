package com.makentoshe.habrachan.common.model.network.users

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.network.ErrorResult
import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetUserByLoginConverterFactory : Converter.Factory() {

    val converter = Converter<ResponseBody, Result.GetUserByLoginResponse> {
        val success = Gson().fromJson(it.string(), GetUserByLoginResult::class.java)
        if (success.data != null) {
            return@Converter Result.GetUserByLoginResponse(success, null)
        } else {
            val error = Gson().fromJson(it.string(), ErrorResult::class.java)
            return@Converter Result.GetUserByLoginResponse(null, error)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.GetUserByLoginResponse>? {
        return if (type == Result.GetUserByLoginResponse::class.java) converter else null
    }
}