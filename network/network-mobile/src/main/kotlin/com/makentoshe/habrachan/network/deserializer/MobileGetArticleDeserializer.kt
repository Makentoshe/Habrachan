package com.makentoshe.habrachan.network.deserializer

import com.makentoshe.habrachan.entity.mobiles.Article

class MobileGetArticleDeserializer: GsonDeserializer() {

    //article_success
    fun body(json: String): Result<Article> = try {
        Result.success(gson.fromJson(json, Article::class.java))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    //article_failure
    fun error(json: String): Result<Article> {
        return Result.failure(Exception(json))
    }
}