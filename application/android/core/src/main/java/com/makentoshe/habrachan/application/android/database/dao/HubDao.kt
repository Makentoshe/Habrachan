package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.HubRecord

// TODO add update method that creates row if it does not exists or update fields instead
@Dao
interface HubDao {

    @Query("SELECT * FROM HubRecord")
    fun getAll(): List<HubRecord>

    @Query("SELECT * FROM HubRecord WHERE hubId = :id")
    fun getById(id: Int): HubRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: HubRecord)

    @Delete
    fun delete(record: HubRecord)

    @Query("DELETE FROM HubRecord")
    fun clear()
}