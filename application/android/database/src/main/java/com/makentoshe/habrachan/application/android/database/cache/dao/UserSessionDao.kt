package com.makentoshe.habrachan.application.android.database.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.makentoshe.habrachan.application.android.database.cache.record.UserSessionRecord

@Dao
interface UserSessionDao {

    @Query("SELECT * FROM UserSessionRecord ORDER BY token DESC LIMIT 1")
    fun get(): UserSessionRecord

    @Insert
    fun insert(record: UserSessionRecord)

    @Query("DELETE FROM UserSessionRecord")
    fun clear()

    @Query("SELECT * FROM UserSessionRecord")
    fun getAll(): List<UserSessionRecord>

    @Transaction
    fun clearAndInsert(record: UserSessionRecord) {
        clear()
        insert(record)
    }
}