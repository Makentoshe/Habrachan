package com.makentoshe.habrachan.common.model.network.votepost

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.network.ErrorResult
import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class VotePostConverterFactory : Converter.Factory() {

    val converter by lazy {
        Converter<ResponseBody, Result.VotePostResponse> {
            val json = it.string()
            val success = Gson().fromJson(json, VotePostResult::class.java)
            if (success.success) {
                return@Converter Result.VotePostResponse(success, null)
            } else {
                val error = Gson().fromJson(json, ErrorResult::class.java)
                return@Converter Result.VotePostResponse(null, error)
            }
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.VotePostResponse>? {
        return if (type == Result.VotePostResponse::class.java) converter else null
    }
}