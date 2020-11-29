package com.makentoshe.habrachan.network.converter

import com.google.gson.GsonBuilder
import com.makentoshe.habrachan.entity.Badges
import com.makentoshe.habrachan.network.deserializer.BadgesDeserializer
import com.makentoshe.habrachan.network.response.ArticlesResponse
import okhttp3.ResponseBody

class ArticlesConverter: Converter<ArticlesResponse> {

    override fun convertBody(body: ResponseBody): Result<ArticlesResponse> {
        val gson = GsonBuilder().registerTypeAdapter(Badges::class.java, BadgesDeserializer()).create()
        val response = gson.fromJson(body.string(), ArticlesResponse::class.java)
        return Result.success(response)
    }

    override fun convertError(body: ResponseBody): Result<ArticlesResponse> {
        return Result.failure(ConverterException(body.toString()))
    }
}