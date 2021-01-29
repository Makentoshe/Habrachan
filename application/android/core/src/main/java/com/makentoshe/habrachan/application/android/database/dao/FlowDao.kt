package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.FlowRecord

// TODO add update method that creates row if it does not exists or update fields instead
@Dao
interface FlowDao {

    @Query("SELECT * FROM FlowRecord")
    fun getAll(): List<FlowRecord>

    @Query("SELECT * FROM FlowRecord WHERE id = :id")
    fun getById(id: Int): FlowRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: FlowRecord)

    @Delete
    fun delete(record: FlowRecord)

    @Query("DELETE FROM FlowRecord")
    fun clear()
}
