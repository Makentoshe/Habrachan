package com.makentoshe.habrachan.network.deserializer

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.makentoshe.habrachan.entity.Badge
import com.makentoshe.habrachan.entity.Badges
import java.lang.reflect.Type

class BadgesDeserializer : JsonDeserializer<Badges> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Badges {
        if (json?.isJsonObject == true) {
            val typeToken = object : TypeToken<Map<String, Badge>>() {}
            val map = Gson().fromJson<Map<String, Badge>>(json, typeToken.type)
            return Badges().apply { addAll(map.values) }
        }
        if (json?.isJsonArray == true) {
            return Gson().fromJson(json, Badges::class.java)
        }
        throw JsonParseException("Badges type is not array and is not map")
    }
}