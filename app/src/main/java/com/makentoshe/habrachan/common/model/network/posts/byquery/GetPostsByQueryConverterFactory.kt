package com.makentoshe.habrachan.common.model.network.posts.byquery

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.network.ErrorResult
import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetPostsByQueryConverterFactory : Converter.Factory() {

    val converter = Converter<ResponseBody, Result.GetPostsByQueryResponse> {
        val json = it.string()
        val success = Gson().fromJson(json, GetPostsByQueryResult::class.java)
        if (success.success) {
            return@Converter Result.GetPostsByQueryResponse(success = success, error = null)
        } else {
            val error = Gson().fromJson(json, ErrorResult::class.java)
            return@Converter Result.GetPostsByQueryResponse(success = null, error = error)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.GetPostsByQueryResponse>? {
        return if (type == Result.GetPostsByQueryResponse::class.java) converter else null
    }

}