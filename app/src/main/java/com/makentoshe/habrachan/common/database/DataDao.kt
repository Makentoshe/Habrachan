package com.makentoshe.habrachan.common.database

import androidx.room.*
import com.makentoshe.habrachan.common.entity.Data

@Dao
interface DataDao {

    @Query("SELECT * FROM data")
    fun getAll(): List<Data>

    @Query("SELECT * FROM data WHERE id = :id")
    fun getById(id: Int): Data?

    @Query("SELECT * FROM data WHERE `index` = :index")
    fun getByIndex(index: Int): Data?

    @Delete
    fun delete(data: Data)

    @Query("DELETE FROM data")
    fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: Data)
}
