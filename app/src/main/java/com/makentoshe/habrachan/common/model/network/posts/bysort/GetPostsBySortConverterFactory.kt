package com.makentoshe.habrachan.common.model.network.posts.bysort

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.network.ErrorResult
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import com.makentoshe.habrachan.common.model.network.Result

class GetPostsBySortConverterFactory : Converter.Factory() {

    val converter = Converter<ResponseBody, Result.GetPostsBySortResponse> {
        val json = it.string()
        val success = Gson().fromJson(json, GetPostsBySortResult::class.java)
        if (success.success) {
            return@Converter Result.GetPostsBySortResponse(success = success, error = null)
        } else {
            val error = Gson().fromJson(json, ErrorResult::class.java)
            return@Converter Result.GetPostsBySortResponse(success = null, error = error)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.GetPostsBySortResponse>? {
        return if (type == Result.GetPostsBySortResponse::class.java) converter else null
    }

}
