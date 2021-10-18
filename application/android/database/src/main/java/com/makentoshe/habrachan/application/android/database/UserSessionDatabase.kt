package com.makentoshe.habrachan.application.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.makentoshe.habrachan.application.android.database.converters.MapTypeConverter
import com.makentoshe.habrachan.application.android.database.dao.UserSessionDao
import com.makentoshe.habrachan.application.android.database.dao.userdatabase.ArticlesFilterDao
import com.makentoshe.habrachan.application.android.database.dao.userdatabase.ArticlesUserSearchArticlesFilterCrossRefDao
import com.makentoshe.habrachan.application.android.database.dao.userdatabase.ArticlesUserSearchDao
import com.makentoshe.habrachan.application.android.database.record.UserRecord
import com.makentoshe.habrachan.application.android.database.record.UserSessionRecord
import com.makentoshe.habrachan.application.android.database.record.userdatabase.ArticlesFilterRecord
import com.makentoshe.habrachan.application.android.database.record.userdatabase.ArticlesUserSearchArticlesFilterCrossRef
import com.makentoshe.habrachan.application.android.database.record.userdatabase.ArticlesUserSearchRecord

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

    abstract fun articlesUserSearchDao(): ArticlesUserSearchDao

    abstract fun articlesFilterDao(): ArticlesFilterDao

    abstract fun articlesUserSearchArticlesFilterCrossRef(): ArticlesUserSearchArticlesFilterCrossRefDao
}