package com.makentoshe.habrachan.application.android.database.cache.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.cache.record.BadgeRecord

@Dao
interface BadgeDao {

    @Query("SELECT * FROM BadgeRecord")
    fun getAll(): List<BadgeRecord>

    @Query("SELECT * FROM BadgeRecord WHERE id = :id")
    fun getById(id: Int): BadgeRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: BadgeRecord)

    @Delete
    fun delete(record: BadgeRecord)

    @Query("DELETE FROM BadgeRecord")
    fun clear()
}