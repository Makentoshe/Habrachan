package com.makentoshe.habrachan.common.model.network

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.entity.GetPostsBySearchResult
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetPostsBySearchResultConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ) = Converter<ResponseBody, GetPostsBySearchResult> {
        Gson().fromJson(it.string(), GetPostsBySearchResult::class.java)
    }
}