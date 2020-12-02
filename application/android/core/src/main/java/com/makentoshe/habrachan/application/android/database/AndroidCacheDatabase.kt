package com.makentoshe.habrachan.application.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.makentoshe.habrachan.application.android.database.converter.BadgesConverter
import com.makentoshe.habrachan.application.android.database.converter.FlowsConverter
import com.makentoshe.habrachan.application.android.database.converter.HubsConverter
import com.makentoshe.habrachan.application.android.database.record.ArticleRecord

/**
 * This is main cache database used in Android application.
 *
 * entities - array of object types that database contains.
 */
@Database(entities = [ArticleRecord::class], version = 1)
@TypeConverters(value = [FlowsConverter::class, BadgesConverter::class, HubsConverter::class])
abstract class AndroidCacheDatabase : RoomDatabase() {

    abstract fun articlesDao(): ArticlesDao
}