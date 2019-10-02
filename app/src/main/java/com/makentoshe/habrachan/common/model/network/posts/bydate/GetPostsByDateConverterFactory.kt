package com.makentoshe.habrachan.common.model.network.posts.bydate

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.network.ErrorResult
import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetPostsByDateConverterFactory : Converter.Factory() {

    val converter = Converter<ResponseBody, Result.GetPostsByDateResponse> {
        val json = it.string()
        val success = Gson().fromJson(json, GetPostsByDateResult::class.java)
        if (success.success) {
            return@Converter Result.GetPostsByDateResponse(success = success, error = null)
        } else {
            val error = Gson().fromJson(json, ErrorResult::class.java)
            return@Converter Result.GetPostsByDateResponse(success = null, error = error)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.GetPostsByDateResponse>? {
        return if (type == Result.GetPostsByDateResponse::class.java) converter else null
    }

}
