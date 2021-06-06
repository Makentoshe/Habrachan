package com.makentoshe.habrachan.network.deserializer

import com.google.gson.GsonBuilder
import com.makentoshe.habrachan.entity.ArticleText
import com.makentoshe.habrachan.network.deserialize.TypeAdapter

abstract class MobileGsonDeserializer {

    val gson = GsonBuilder().apply {
        registerTypeAdapter(ArticleText::class.java, TypeAdapter())
    }.create()
}
