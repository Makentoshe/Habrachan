package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.entity.mobiles.Article

class MobileGetArticleDeserializer {

    fun body(json: String): Result<Article> {
        return Result.success(Gson().fromJson(json, Article::class.java))
    }

    fun error(json: String): Result<Article> {
        return Result.failure(Exception(json))
    }
}