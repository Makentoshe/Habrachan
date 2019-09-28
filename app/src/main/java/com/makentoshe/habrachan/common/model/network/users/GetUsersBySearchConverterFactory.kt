package com.makentoshe.habrachan.common.model.network.users

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetUsersBySearchConverterFactory : Converter.Factory() {

    private val converter by lazy {
        Converter<ResponseBody, GetUsersBySearchResult> {
            Gson().fromJson(it.string(), GetUsersBySearchResult::class.java)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, GetUsersBySearchResult>? {
        return if (type == GetUsersBySearchResult::class.java) converter else null
    }
}

