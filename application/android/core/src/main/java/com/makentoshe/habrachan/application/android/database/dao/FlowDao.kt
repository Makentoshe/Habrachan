package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.FlowRecord

@Dao
interface FlowDao {

    @Query("SELECT * FROM FlowRecord")
    fun getAll(): List<FlowRecord>

    @Query("SELECT * FROM FlowRecord WHERE flowId = :id")
    fun getById(id: Int): FlowRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: FlowRecord)

    @Delete
    fun delete(record: FlowRecord)

    @Query("DELETE FROM FlowRecord")
    fun clear()
}
