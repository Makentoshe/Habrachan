package com.makentoshe.habrachan.application.android.database.converter

import androidx.room.TypeConverter
import com.makentoshe.habrachan.application.android.database.record.BadgesRecord

class BadgesConverter {

    @TypeConverter
    fun badgesToJson(badges: BadgesRecord): String = badges.toJson()

    @TypeConverter
    fun jsonToBadges(json: String): BadgesRecord = BadgesRecord.fromJson(json)
}