package com.makentoshe.habrachan.common.model.network.flows

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.network.ErrorResult
import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetFlowsConverterFactory : Converter.Factory() {

    val converter by lazy {
        Converter<ResponseBody, Result.GetFlowsResponse> {
            val result = Gson().fromJson(it.string(), GetFlowsResult::class.java)
            if (result.data == null) {
                val error = Gson().fromJson(it.string(), ErrorResult::class.java)
                return@Converter Result.GetFlowsResponse(success = null, error = error)
            }
            return@Converter Result.GetFlowsResponse(success = result, error = null)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.GetFlowsResponse>? {
        return if (type == Result.GetFlowsResponse::class.java) converter else null
    }
}
