package com.makentoshe.habrachan.common.network.converter

import com.google.gson.GsonBuilder
import com.makentoshe.habrachan.common.entity.Badges
import com.makentoshe.habrachan.common.network.response.ArticlesResponse
import okhttp3.ResponseBody

class ArticlesConverter {

    fun convertBody(body: ResponseBody): ArticlesResponse.Success {
        val gson = GsonBuilder().registerTypeAdapter(Badges::class.java, BadgesDeserializer()).create()
        return gson.fromJson(body.string(), ArticlesResponse.Success::class.java)
    }

    fun convertError(body: ResponseBody): ArticlesResponse.Error {
        return ArticlesResponse.Error(body.string())
    }
}