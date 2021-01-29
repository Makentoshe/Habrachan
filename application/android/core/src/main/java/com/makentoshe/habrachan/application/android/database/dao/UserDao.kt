package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.UserRecord

@Dao
interface UserDao {

    @Query("SELECT * FROM UserRecord")
    fun getAll(): List<UserRecord>

    @Query("SELECT * FROM UserRecord WHERE id = :id")
    fun getById(id: Int): UserRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: UserRecord)

    @Delete
    fun delete(record: UserRecord)

    @Query("DELETE FROM UserRecord")
    fun clear()
}