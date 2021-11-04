package com.makentoshe.habrachan.application.android.database.user.migration

import androidx.room.migration.Migration

val userDatabaseMigrations: Array<Migration>
    get() = arrayOf(UserDatabaseMigrationFrom1To2())