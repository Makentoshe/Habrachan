package com.makentoshe.habrachan.application.android.database.cache.dao

import androidx.room.Dao
import androidx.room.Query
import com.makentoshe.habrachan.application.android.database.cache.record.UserRecord

@Dao
interface UserDao {

    @Query("SELECT * FROM UserRecord")
    fun getAll(): List<UserRecord>
}