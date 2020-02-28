package com.makentoshe.habrachan.common.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.makentoshe.habrachan.common.entity.Badge
import com.makentoshe.habrachan.common.entity.Flow
import com.makentoshe.habrachan.common.entity.Hub

class Converters {

    @TypeConverter
    fun hubsToJson(hubs: List<Hub>): String {
        val jsonArray = JsonArray()
        hubs.map { it.toJson() }.forEach(jsonArray::add)
        return jsonArray.toString()
    }

    @TypeConverter
    fun jsonToHubs(json: String): List<Hub> {
        val jsonArray = Gson().fromJson(json, JsonArray::class.java)
        return jsonArray.map { it.asString }.map { Hub.fromJson(it) }
    }

    @TypeConverter
    fun flowsToJson(flows: List<Flow>): String {
        val jsonArray = JsonArray()
        flows.map { it.toJson() }.forEach(jsonArray::add)
        return jsonArray.toString()
    }

    @TypeConverter
    fun jsonToFlows(json: String): List<Flow> {
        val jsonArray = Gson().fromJson(json, JsonArray::class.java)
        return jsonArray.map { it.asString }.map { Flow.fromJson(it) }
    }

    @TypeConverter
    fun badgesToJson(flows: List<Badge>): String {
        val jsonArray = JsonArray()
        flows.map { it.toJson() }.forEach(jsonArray::add)
        return jsonArray.toString()
    }

    @TypeConverter
    fun jsonToBadges(json: String): List<Badge> {
        val jsonArray = Gson().fromJson(json, JsonArray::class.java)
        return jsonArray.map { it.asString }.map { Badge.fromJson(it) }
    }

    @TypeConverter
    fun anyToJson(any: Any?): String {
        return any?.toString() ?: "null"
    }

    @TypeConverter
    fun jsonToAny(json: String): Any {
        return json
    }
}