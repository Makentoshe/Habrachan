package com.makentoshe.habrachan.application.android.database.migration.userdatabase

import androidx.room.migration.Migration

val userDatabaseMigrations: Array<Migration>
    get() = arrayOf(UserDatabaseMigrationFrom1To2())