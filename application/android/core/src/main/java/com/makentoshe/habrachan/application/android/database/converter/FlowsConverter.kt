package com.makentoshe.habrachan.application.android.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.makentoshe.habrachan.application.android.database.record.FlowRecord

// TODO create Flows class and move json converts here

fun flowRecordToJson(flowRecord: FlowRecord): String {
    return Gson().toJson(flowRecord)
}

fun jsonToFlowRecord(json: String): FlowRecord {
    return Gson().fromJson(json, FlowRecord::class.java)
}

class FlowsConverter {

    @TypeConverter
    fun flowsToJson(flows: List<FlowRecord>): String {
        val jsonArray = JsonArray()
        flows.map(::flowRecordToJson).forEach(jsonArray::add)
        return jsonArray.toString()
    }

    @TypeConverter
    fun jsonToFlows(json: String): List<FlowRecord> {
        val jsonArray = Gson().fromJson(json, JsonArray::class.java)
        return jsonArray.map { it.asString }.map(::jsonToFlowRecord)
    }

}