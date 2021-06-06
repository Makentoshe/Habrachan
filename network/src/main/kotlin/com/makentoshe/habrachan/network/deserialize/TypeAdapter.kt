package com.makentoshe.habrachan.network.deserialize

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.makentoshe.habrachan.entity.ArticleText
import java.lang.reflect.Type

class TypeAdapter: JsonDeserializer<ArticleText> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ArticleText? {
        return json?.asString?.let(::ArticleText)
    }
}