package com.makentoshe.habrachan.application.android.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class AndroidCacheDatabaseMigrationF1T2: Migration(1,2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // do nothing here because we just add additional daos to our table
    }
}