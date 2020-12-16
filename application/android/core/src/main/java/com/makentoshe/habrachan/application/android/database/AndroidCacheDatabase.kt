package com.makentoshe.habrachan.application.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.makentoshe.habrachan.application.android.database.converter.BadgesConverter
import com.makentoshe.habrachan.application.android.database.converter.FlowsConverter
import com.makentoshe.habrachan.application.android.database.converter.HubsConverter
import com.makentoshe.habrachan.application.android.database.record.ArticleRecord
import com.makentoshe.habrachan.application.android.database.record.AvatarRecord

/**
 * This is main cache database used in Android application.
 *
 * entities - array of object types that database contains.
 */
// TODO rework recording system - separate objects between tables and bind it with id's
@Database(entities = [ArticleRecord::class, AvatarRecord::class], version = 1)
@TypeConverters(value = [FlowsConverter::class, BadgesConverter::class, HubsConverter::class])
abstract class AndroidCacheDatabase : RoomDatabase() {

    /** Contains articles from last search */
    abstract fun articlesSearchDao(): ArticlesDao

    /** Stores indexed avatars paths. The real files stores in file system cache */
    abstract fun avatarDao(): AvatarDao
}