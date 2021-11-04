package com.makentoshe.habrachan.application.android.database.user.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.application.android.database.user.record.ArticlesFilterRecord

@Dao
interface ArticlesFilterDao {

    @Query("SELECT * FROM ArticlesFilterRecord")
    fun getAll(): List<ArticlesFilterRecord>

    @Query("SELECT * FROM ArticlesFilterRecord WHERE internalType = :type")
    fun getByType(type: String): List<ArticlesFilterRecord>

    @Query("SELECT * FROM ArticlesFilterRecord WHERE keyValuePair = :keyValuePair")
    fun getByKeyValue(keyValuePair: String): ArticlesFilterRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: ArticlesFilterRecord)

}