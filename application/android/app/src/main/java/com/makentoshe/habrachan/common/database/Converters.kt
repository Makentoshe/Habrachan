package com.makentoshe.habrachan.common.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.makentoshe.habrachan.common.entity.Badges
import com.makentoshe.habrachan.common.entity.Flow
import com.makentoshe.habrachan.common.entity.Hub
import com.makentoshe.habrachan.common.entity.session.ArticlesRequestSpec

class HubsConverter {

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
}

class FlowsConverter {

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

}

class BadgesConverter {

    @TypeConverter
    fun badgesToJson(badges: Badges) = badges.toJson()

    @TypeConverter
    fun jsonToBadges(json: String) = Badges.fromJson(json)
}

class ArticlesRequestSpecConverter {

    @TypeConverter
    fun specToJson(spec: ArticlesRequestSpec): String {
        return spec.toJson()
    }

    @TypeConverter
    fun jsonToSpec(json: String): ArticlesRequestSpec {
        return ArticlesRequestSpec.fromJson(json)
    }
}
