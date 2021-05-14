package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.FlowRecord2

@Dao
interface FlowDao2 {

    @Query("SELECT * FROM FlowRecord2")
    fun getAll(): List<FlowRecord2>

    @Query("SELECT * FROM FlowRecord2 WHERE flowId = :flowId")
    fun getById(flowId: Int): FlowRecord2?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: FlowRecord2)

    @Delete
    fun delete(record: FlowRecord2)

    @Query("DELETE FROM FlowRecord2")
    fun clear()
}