package com.makentoshe.habrachan.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.makentoshe.habrachan.common.entity.Data

@Database(entities = [Data::class], version = 1)
@TypeConverters(Converters::class)
abstract class HabrDatabase : RoomDatabase() {
    abstract fun posts(): PostsDao
}