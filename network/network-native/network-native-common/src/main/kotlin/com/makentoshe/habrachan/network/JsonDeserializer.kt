package com.makentoshe.habrachan.network

import com.google.gson.GsonBuilder
import com.makentoshe.habrachan.entity.ArticleText
import com.makentoshe.habrachan.network.deserialize.TypeAdapter

abstract class NativeGsonDeserializer {

    val gson = GsonBuilder().apply {
        registerTypeAdapter(ArticleText::class.java, TypeAdapter())
    }.create()
}
