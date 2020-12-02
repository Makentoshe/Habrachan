package com.makentoshe.habrachan.application.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.makentoshe.habrachan.application.android.database.record.ArticleRecord

/**
 * This is main cache database used in Android application.
 *
 * entities - array of object types that database contains.
 */
@Database(entities = [ArticleRecord::class], version = 1)
abstract class AndroidCacheDatabase: RoomDatabase() {

    abstract fun articlesDao(): ArticlesDao
}