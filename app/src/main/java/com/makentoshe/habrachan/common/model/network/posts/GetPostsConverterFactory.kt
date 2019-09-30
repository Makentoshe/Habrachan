package com.makentoshe.habrachan.common.model.network.posts

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.network.ErrorResult
import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetPostsConverterFactory : Converter.Factory() {

    val converter = Converter<ResponseBody, Result.GetPostsResponse> {
        val json = it.string()
        val success = Gson().fromJson(json, GetPostsResult::class.java)
        if (success.success) {
            return@Converter Result.GetPostsResponse(success = success, error = null)
        } else {
            val error = Gson().fromJson(json, ErrorResult::class.java)
            return@Converter Result.GetPostsResponse(success = null, error = error)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.GetPostsResponse>? {
        return if (type == Result.GetPostsResponse::class.java) converter else null
    }

}
