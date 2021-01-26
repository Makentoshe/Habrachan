package com.makentoshe.habrachan.application.android.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class AndroidCacheDatabaseMigration12 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // We didn't alter the table, so there is nothing to do here
    }
}