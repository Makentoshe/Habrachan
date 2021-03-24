package com.makentoshe.habrachan.application.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.makentoshe.habrachan.application.android.database.dao.UserSessionDao
import com.makentoshe.habrachan.application.android.database.record.*

/**
 * This is a database with user-sensitive records used in the Android application.
 *
 * entities - array of object types that database contains.
 */
@Database(
    entities = [
        UserSessionRecord::class
    ],
    version = 1
)
abstract class UserSessionDatabase : RoomDatabase() {

    abstract fun userSessionDao(): UserSessionDao
}