package com.makentoshe.habrachan.application.android.database.dao.userdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.makentoshe.habrachan.application.android.database.record.userdatabase.ArticlesUserSearchRecord

@Dao
interface ArticlesUserSearchDao {

    @Query("SELECT * FROM ArticlesUserSearchRecord")
    fun getAll(): List<ArticlesUserSearchRecord>

    @Query("SELECT * FROM ArticlesUserSearchRecord LIMIT 1 OFFSET :index")
    fun getByIndex(index: Int): ArticlesUserSearchRecord?

    @Insert
    fun insert(record: ArticlesUserSearchRecord)

}