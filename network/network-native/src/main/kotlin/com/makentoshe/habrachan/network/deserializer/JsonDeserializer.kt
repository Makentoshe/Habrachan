package com.makentoshe.habrachan.network.deserializer

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.makentoshe.habrachan.entity.ArticleText
import java.lang.reflect.Type

abstract class GsonDeserializer {

    val gson = GsonBuilder().apply {
        registerTypeAdapter(ArticleText::class.java, TypeAdapter())
    }.create()
}

class TypeAdapter: JsonDeserializer<ArticleText> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ArticleText? {
        return json?.asString?.let(::ArticleText)
    }
}
