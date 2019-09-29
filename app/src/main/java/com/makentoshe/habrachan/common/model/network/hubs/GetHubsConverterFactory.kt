package com.makentoshe.habrachan.common.model.network.hubs

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.network.ErrorResult
import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetHubsConverterFactory : Converter.Factory() {

    val converter = Converter<ResponseBody, Result.GetHubsResponse> {
        val json = it.string()
        val success = Gson().fromJson(json, GetHubsResult::class.java)
        if (success.data == null || success.pages == null || success.nextPage == null || success.serverTime == null) {
            val error = Gson().fromJson(json, ErrorResult::class.java)
            return@Converter Result.GetHubsResponse(success = null, error = error)
        } else {
            return@Converter Result.GetHubsResponse(success = success, error = null)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.GetHubsResponse>? {
        return if (type == Result.GetHubsResponse::class.java) converter else null
    }
}