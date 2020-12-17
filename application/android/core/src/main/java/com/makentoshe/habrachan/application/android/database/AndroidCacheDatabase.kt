package com.makentoshe.habrachan.application.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.makentoshe.habrachan.application.android.database.dao.BadgeDao
import com.makentoshe.habrachan.application.android.database.dao.FlowDao
import com.makentoshe.habrachan.application.android.database.dao.HubDao
import com.makentoshe.habrachan.application.android.database.record.*

/**
 * This is main cache database used in Android application.
 * entities - array of object types that database contains.
 */
@Database(
    entities = [ArticleRecord::class, AvatarRecord::class, FlowRecord::class, HubRecord::class, BadgeRecord::class],
    version = 1
)
abstract class AndroidCacheDatabase : RoomDatabase() {

    /** Contains articles from last search. Potential infinite.
     * That means that the cache should be released occasionally */
    abstract fun articlesSearchDao(): ArticlesDao

    /** Stores indexed avatars paths. The real files stores in file system cache.
     * Cache should be released occasionally with file system.
     */
    abstract fun avatarDao(): AvatarDao

    /**
     * Stores flows from different sources. Do not infinite,
     * so we do not care of releasing cache memory
     */
    abstract fun flowDao(): FlowDao

    /**
     * Stores different articles hubs. Do not infinite,
     * so we do not care of releasing cache memory
     */
    abstract fun hubDao(): HubDao

    /**
     * Stores different user badges. Do not infinite,
     * so we do not care of releasing cache memory
     */
    abstract fun badgeDao(): BadgeDao
}