package com.makentoshe.habrachan.application.android.database.user

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.makentoshe.habrachan.application.android.database.MapTypeConverter
import com.makentoshe.habrachan.application.android.database.cache.dao.UserSessionDao
import com.makentoshe.habrachan.application.android.database.cache.record.UserRecord
import com.makentoshe.habrachan.application.android.database.cache.record.UserSessionRecord
import com.makentoshe.habrachan.application.android.database.user.dao.ArticlesFilterDao
import com.makentoshe.habrachan.application.android.database.user.dao.ArticlesUserSearchArticlesFilterCrossRefDao
import com.makentoshe.habrachan.application.android.database.user.dao.ArticlesUserSearchDao
import com.makentoshe.habrachan.application.android.database.user.record.ArticlesFilterRecord
import com.makentoshe.habrachan.application.android.database.user.record.ArticlesUserSearchArticlesFilterCrossRef
import com.makentoshe.habrachan.application.android.database.user.record.ArticlesUserSearchRecord

/**
 * This is a database with user-sensitive records used in the Android application.
 * That means that database requires migrations and other stuff for the best user experience.
 *
 * entities - array of object types that database contains.
 */
@Database(
    version = 2,
    entities = [
        UserSessionRecord::class,
        UserRecord::class,
        ArticlesUserSearchRecord::class,
        ArticlesFilterRecord::class,
        ArticlesUserSearchArticlesFilterCrossRef::class
    ],
)
@TypeConverters(MapTypeConverter::class)
abstract class UserSessionDatabase : RoomDatabase() {

    abstract fun userSessionDao(): UserSessionDao

    /** A part of article searches by user contains mostly meta-info such as user-defined title */
    abstract fun articlesUserSearchDao(): ArticlesUserSearchDao

    /** A part of article searches by user contains queries data */
    abstract fun articlesFilterDao(): ArticlesFilterDao

    /** A part of article searches. Allows to map titles and queries */
    abstract fun articlesUserSearchArticlesFilterCrossRef(): ArticlesUserSearchArticlesFilterCrossRefDao
}