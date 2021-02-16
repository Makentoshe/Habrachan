package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.entity.mobiles.Article

class MobileGetArticleDeserializer {

    //article_success
    fun body(json: String): Result<Article> {
        return Result.success(Gson().fromJson(json, Article::class.java))
    }

    //article_failure
    fun error(json: String): Result<Article> {
        return Result.failure(Exception(json))
    }
}