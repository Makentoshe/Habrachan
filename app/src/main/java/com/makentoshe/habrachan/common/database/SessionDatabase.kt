package com.makentoshe.habrachan.common.database

import android.content.Context

class SessionDatabase(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("Session", Context.MODE_PRIVATE)

    val api: String
        get() = sharedPreferences.getString("api", "173984950848a2d27c0cc1c76ccf3d6d3dc8255b")!!

    val client: String
        get() = sharedPreferences.getString("client", "85cab69095196f3.89453480")!!

    var token: String?
        get() = sharedPreferences.getString("token", null)
        set(value) = sharedPreferences.edit().putString("token", value).apply()
}