package com.makentoshe.habrachan.network.deserializer

import com.google.gson.*
import com.makentoshe.habrachan.entity.natives.Geo
import java.lang.reflect.Type

class GeoDeserializer : JsonDeserializer<Geo> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Geo {
        if (json?.isJsonObject == true) {
            return Gson().fromJson(json, Geo::class.java)
        }
        if (json?.isJsonNull == true) {
            return Geo()
        }
        throw JsonParseException("Geo type is not null and is not object")
    }
}