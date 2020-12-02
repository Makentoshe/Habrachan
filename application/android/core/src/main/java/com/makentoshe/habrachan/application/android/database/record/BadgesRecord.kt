package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import com.google.gson.Gson
import com.makentoshe.habrachan.entity.Badges

@Entity
class BadgesRecord : ArrayList<BadgeRecord>() {

    fun toBadges(): Badges {
        val badges = Badges()
        badges.addAll(map { it.toBadge() })
        return badges
    }

    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(json: String): BadgesRecord = Gson().fromJson(json, BadgesRecord::class.java)
    }
}