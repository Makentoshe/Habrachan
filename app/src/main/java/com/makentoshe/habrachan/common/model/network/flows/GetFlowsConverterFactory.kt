package com.makentoshe.habrachan.common.model.network.flows

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.network.ErrorResult
import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetFlowsConverterFactory : Converter.Factory() {

    private val converter by lazy {
        Converter<ResponseBody, Result.GetFlowsResult2> {
            val result = Gson().fromJson(it.string(), GetFlowsResult::class.java)
            if (result.data == null && result.serverTime == null) {
                val error = Gson().fromJson(it.string(), ErrorResult::class.java)
                return@Converter Result.GetFlowsResult2(success = null, error = error)
            }
            return@Converter Result.GetFlowsResult2(success = result, error = null)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.GetFlowsResult2>? {
        return if (type == Result.GetFlowsResult2::class.java) converter else null
    }
}
