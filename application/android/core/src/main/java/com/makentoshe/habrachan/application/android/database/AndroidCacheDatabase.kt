package com.makentoshe.habrachan.application.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.makentoshe.habrachan.application.android.database.dao.*
import com.makentoshe.habrachan.application.android.database.dao.article.InterestingArticlesDao
import com.makentoshe.habrachan.application.android.database.dao.article.NewArticlesDao
import com.makentoshe.habrachan.application.android.database.dao.article.TempArticlesDao
import com.makentoshe.habrachan.application.android.database.dao.article.TopArticlesDao
import com.makentoshe.habrachan.application.android.database.record.*
import com.makentoshe.habrachan.application.android.database.record.article.InterestingArticleRecord
import com.makentoshe.habrachan.application.android.database.record.article.NewArticleRecord
import com.makentoshe.habrachan.application.android.database.record.article.TempArticleRecord
import com.makentoshe.habrachan.application.android.database.record.article.TopArticleRecord

/**
 * This is a cache database used in the Android application.
 * Caches means that if it will be disposed - there aren't a great problem.
 *
 * entities - array of object types that database contains.
 */
@Database(
    entities = [
        AvatarRecord::class,
        ContentRecord::class,

        BadgeRecord::class,
        CommentRecord::class,

        ArticleRecord2::class,
        HubRecord2::class,
        FlowRecord2::class,
        ArticleHubCrossRef::class,
        ArticleFlowCrossRef::class,
        ArticleAuthorRecord::class,

        FlowRecord::class,
        HubRecord::class,

        TopArticleRecord::class,
        InterestingArticleRecord::class,
        NewArticleRecord::class,
        TempArticleRecord::class
    version = 6
)
abstract class AndroidCacheDatabase : RoomDatabase() {

    /**
     * Contains articles from direct load. Potential infinite.
     * That means that the cache should be released occasionally
     * */
    abstract fun tempArticlesDao(): TempArticlesDao

    /**
     * Contains articles from last search. Potential infinite.
     * That means that the cache should be released occasionally
     * */
    abstract fun newArticlesDao(): NewArticlesDao

    /**
     * Contains articles from last search. Potential infinite.
     * That means that the cache should be released occasionally
     * */
    abstract fun interestingArticlesDao(): InterestingArticlesDao

    /**
     * Contains articles from last search. Potential infinite.
     * That means that the cache should be released occasionally
     * */
    abstract fun topArticlesDao(): TopArticlesDao
    /** Contains articles from last search. Potential infinite.
     * That means that the cache should be released occasionally */
    abstract fun articlesDao2(): ArticlesDao2

    abstract fun articleAuthorDao(): ArticleAuthorDao

    /**
     * Stores different articles hubs. Do not infinite,
     * so we do not care of releasing cache memory
     */
    abstract fun hubDao2(): HubDao2

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
     * Stores flows from different sources. Do not infinite,
     * so we do not care of releasing cache memory
     */
    abstract fun flowDao2(): FlowDao2

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
     * Stores comments for articles. Potential infinite.
     * That means that the cache should be released occasionally.
     */
    abstract fun userDao(): UserDao

    /**
     * Stores indexed contents paths, images as usual. The real files stores in file system cache.
     * Cache should be released occasionally with file system.
     */
    abstract fun contentDao(): ContentDao
}