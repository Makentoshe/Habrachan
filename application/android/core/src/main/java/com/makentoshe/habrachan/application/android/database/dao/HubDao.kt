package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.HubRecord

@Dao
interface HubDao {

    @Query("SELECT * FROM HubRecord")
    fun getAll(): List<HubRecord>

    @Query("SELECT * FROM HubRecord WHERE id = :id")
    fun getById(id: Int): HubRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: HubRecord)

    @Delete
    fun delete(record: HubRecord)

    @Query("DELETE FROM HubRecord")
    fun clear()
}