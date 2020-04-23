package com.makentoshe.habrachan.common.entity

import com.google.gson.Gson

class Badges : ArrayList<Badge>() {

    fun toJson() = Gson().toJson(this)!!

    companion object {
        fun fromJson(string: String) = Gson().fromJson(string, Badges::class.java)!!
    }
}