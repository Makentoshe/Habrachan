package com.makentoshe.habrachan.common.network.converter

import com.google.gson.GsonBuilder
import com.makentoshe.habrachan.common.entity.Badges
import com.makentoshe.habrachan.common.network.response.ArticleResponse
import okhttp3.ResponseBody

class ArticleConverter {

    fun convertBody(body: ResponseBody): ArticleResponse {
        val gson = GsonBuilder().registerTypeAdapter(Badges::class.java, BadgesDeserializer()).create()
        return gson.fromJson(body.string(), ArticleResponse.Success::class.java)
    }

    fun convertError(body: ResponseBody): ArticleResponse {
        return ArticleResponse.Error(body.string())
    }
}