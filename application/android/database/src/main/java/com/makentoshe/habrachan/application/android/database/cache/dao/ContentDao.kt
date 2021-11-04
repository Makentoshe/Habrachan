package com.makentoshe.habrachan.application.android.database.cache.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.cache.record.ContentRecord

@Dao
interface ContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: ContentRecord)

    @Query("SELECT * FROM ContentRecord")
    fun getAll(): List<ContentRecord>

    @Query("SELECT COUNT(*) FROM ContentRecord")
    fun count(): Long

    @Query("SELECT * FROM ContentRecord WHERE id IN (SELECT id FROM ContentRecord ORDER BY id ASC LIMIT :n)")
    fun last(n: Int): List<ContentRecord>

    @Delete
    fun delete(record: ContentRecord)

    @Query("DELETE FROM ContentRecord")
    fun clear()
}