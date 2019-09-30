package com.makentoshe.habrachan.common.model.network.users

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.network.ErrorResult
import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetUsersBySearchConverterFactory : Converter.Factory() {

    val converter = Converter<ResponseBody, Result.GetUsersBySearchResponse> {
        val json = it.string()
        val success = Gson().fromJson(json, GetUsersBySearchResult::class.java)
        if (success.success) {
            return@Converter Result.GetUsersBySearchResponse(success = success, error = null)
        } else {
            val error = Gson().fromJson(json, ErrorResult::class.java)
            return@Converter Result.GetUsersBySearchResponse(success = null, error = error)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.GetUsersBySearchResponse>? {
        return if (type == Result.GetUsersBySearchResponse::class.java) converter else null
    }
}

