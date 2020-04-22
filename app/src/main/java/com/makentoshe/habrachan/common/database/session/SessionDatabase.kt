package com.makentoshe.habrachan.common.database.session

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.makentoshe.habrachan.common.entity.session.UserSession

@Database(entities = [UserSession::class], version = 1)
@TypeConverters(SessionConverters::class)
abstract class SessionDatabase : RoomDatabase() {
    abstract fun session(): SessionDao
}
