package com.makentoshe.habrachan.application.android.database.user.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal class UserDatabaseMigrationFrom1To2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) = database.execSQL(
        """
        CREATE TABLE IF NOT EXISTS ArticlesUserSearchRecord (
            `title` TEXT NOT NULL,
            `params` TEXT NOT NULL,
            PRIMARY KEY(`title`)
        );  
        """.trimIndent()
    )
}