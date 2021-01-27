package com.makentoshe.habrachan.application.android.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("ClassName") // ignore for better understandable
class AndroidCacheDatabaseMigration_1_2 : Migration(1, 2) {

    // Add CommentRecord table for the AndroidCacheDatabase(HabrachanCache)
    override fun migrate(database: SupportSQLiteDatabase) = database.execSQL("""
        CREATE TABLE CommentRecord (
            author_login TEXT NOT NULL,
            level INTEGER NOT NULL,
            isCanVote INTEGER NOT NULL,
            articleId INTEGER NOT NULL,
            avatar TEXT NOT NULL,
            thread INTEGER NOT NULL,
            message TEXT NOT NULL,
            timePublished TEXT NOT NULL,
            parentId INTEGER NOT NULL,
            isAuthor INTEGER NOT NULL, 
            score INTEGER NOT NULL,
            timeChanged TEXT,
            id INTEGER NOT NULL PRIMARY KEY,
            isFavorite INTEGER NOT NULL
        );  
    """.trimIndent())
}
