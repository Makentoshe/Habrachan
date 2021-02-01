package com.makentoshe.habrachan.network.converter

import com.google.gson.GsonBuilder
import com.makentoshe.habrachan.entity.natives.Badges
import com.makentoshe.habrachan.natives.network.response.GetArticlesResponse
import com.makentoshe.habrachan.network.deserializer.BadgesDeserializer
import okhttp3.ResponseBody

class ArticlesConverter: Converter<GetArticlesResponse> {

    override fun convertBody(body: ResponseBody): Result<GetArticlesResponse> {
        val gson = GsonBuilder().registerTypeAdapter(Badges::class.java, BadgesDeserializer()).create()
        val response = gson.fromJson(body.string(), GetArticlesResponse::class.java)
        return Result.success(response)
    }

    override fun convertError(body: ResponseBody): Result<GetArticlesResponse> {
        return Result.failure(ConverterException(body.string()))
    }
}