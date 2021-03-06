package com.makentoshe.habrachan.application.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.makentoshe.habrachan.application.android.database.dao.*
import com.makentoshe.habrachan.application.android.database.record.*

/**
 * This is a cache database used in the Android application.
 * Caches means that if it will be disposed - there aren't a great problem.
 *
 * entities - array of object types that database contains.
 */
@Database(
    entities = [
        AvatarRecord::class,
        BadgeRecord::class,
        CommentRecord::class,
        ContentRecord::class,

        FlowRecord2::class,
        HubRecord2::class,

        ArticleRecord2::class,
        ArticleHubCrossRef::class,
        ArticleFlowCrossRef::class,
        ArticleAuthorRecord::class,
    ], version = 8
)
abstract class AndroidCacheDatabase : RoomDatabase() {

    /**
     * Contains articles from last search. Potential infinite.
     * That means that the cache should be released occasionally
     * */
    abstract fun articlesDao(): ArticlesDao2

    /**
     * Stores authors from last searches. Potential infinite.
     * That means that the cache should be released occasionally.
     */
    abstract fun articleAuthorDao(): ArticleAuthorDao

    /**
     * Stores indexed avatars paths. The real files stores in file system cache.
     * Cache should be released occasionally with file system.
     */
    abstract fun avatarDao(): AvatarDao

    /**
     * Stores flows from different sources. Do not infinite,
     * so we do not care of releasing cache memory
     */
    abstract fun flowDao(): FlowDao2

    /**
     * Stores different articles hubs. Do not infinite,
     * so we do not care of releasing cache memory
     */
    abstract fun hubDao(): HubDao2

    /**
     * Stores different user badges. Do not infinite,
     * so we do not care of releasing cache memory
     */
    abstract fun badgeDao(): BadgeDao

    /**
     * Stores comments for articles. Potential infinite.
     * That means that the cache should be released occasionally.
     */
    abstract fun commentDao(): CommentDao

    /**
     * Stores indexed contents paths, images as usual. The real files stores in file system cache.
     * Cache should be released occasionally with file system.
     */
    abstract fun contentDao(): ContentDao

    /**
     * Stores references between articles and flows. Potential infinite.
     * That means that the cache should be released occasionally.
     */
    abstract fun articleFlowCrossRefDao(): ArticleFlowCrossRefDao

    /**
     * Stores references between articles and hubs. Potential infinite.
     * That means that the cache should be released occasionally.
     */
    abstract fun articleHubCrossRefDao(): ArticleHubCrossRefDao
}