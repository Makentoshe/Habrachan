package com.makentoshe.habrachan.network.converter

import com.google.gson.GsonBuilder
import com.makentoshe.habrachan.entity.Badges
import com.makentoshe.habrachan.network.deserializer.BadgesDeserializer
import com.makentoshe.habrachan.network.response.ArticleResponse
import okhttp3.ResponseBody

class ArticleConverter: Converter<ArticleResponse> {

    override fun convertBody(body: ResponseBody): Result<ArticleResponse> {
        val gson = GsonBuilder().registerTypeAdapter(Badges::class.java, BadgesDeserializer()).create()
        val response = gson.fromJson(body.string(), ArticleResponse::class.java)
        return Result.success(response)
    }

    override fun convertError(body: ResponseBody): Result<ArticleResponse> {
        return Result.failure(ConverterException(body.toString()))
    }
}