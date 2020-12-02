package com.makentoshe.habrachan.application.android.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.makentoshe.habrachan.application.android.database.record.HubRecord

// TODO create Hubs class and move json converts here

fun hubRecordToJson(hubRecord: HubRecord): String {
    return Gson().toJson(hubRecord)
}

fun jsonToHubRecord(json: String): HubRecord {
    return Gson().fromJson(json, HubRecord::class.java)
}

class HubsConverter {

    @TypeConverter
    fun hubsToJson(hubs: List<HubRecord>): String {
        val jsonArray = JsonArray()
        hubs.map(::hubRecordToJson).forEach(jsonArray::add)
        return jsonArray.toString()
    }

    @TypeConverter
    fun jsonToHubs(json: String): List<HubRecord> {
        val jsonArray = Gson().fromJson(json, JsonArray::class.java)
        return jsonArray.map { it.asString }.map(::jsonToHubRecord)
    }
}

