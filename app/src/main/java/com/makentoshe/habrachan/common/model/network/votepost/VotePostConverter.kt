package com.makentoshe.habrachan.common.model.network.votepost

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.makentoshe.habrachan.common.model.network.ErrorResult
import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter

class VotePostConverter: Converter<ResponseBody, Result.VotePostResponse> {

    override fun convert(value: ResponseBody): Result.VotePostResponse {
        val json = value.string()
        val success = parseJson(json)
        val error = if (success == null) parseErrorJson(json) else null
        return Result.VotePostResponse(success = success, error = error)
    }

    private fun parseJson(json: String): VotePostResult?  {
        val result = Gson().fromJson(json, VotePostResult::class.java)
        return if (result.success) result else null
    }

    private fun parseErrorJson(json: String): ErrorResult {
        val errorJson = Gson().fromJson(json, JsonObject::class.java)
        return if (!errorJson["data"].isJsonNull) {
            Gson().fromJson(errorJson["data"].asJsonObject.toString(), ErrorResult::class.java)
        } else {
            Gson().fromJson(errorJson, ErrorResult::class.java)
        }
    }
}