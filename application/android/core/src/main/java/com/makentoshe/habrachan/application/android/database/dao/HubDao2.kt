package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.HubRecord2

@Dao
interface HubDao2 {

    @Query("SELECT * FROM HubRecord2")
    fun getAll(): List<HubRecord2>

    @Query("SELECT * FROM HubRecord2 WHERE hubId = :id")
    fun getById(id: Int): HubRecord2?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: HubRecord2)

    @Delete
    fun delete(record: HubRecord2)

    @Query("DELETE FROM HubRecord2")
    fun clear()
}