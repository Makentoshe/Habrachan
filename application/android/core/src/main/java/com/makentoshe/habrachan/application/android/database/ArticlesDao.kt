package com.makentoshe.habrachan.application.android.database

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.ArticleRecord

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM articlerecord")
    fun getAll(): List<ArticleRecord?>?

    @Query("SELECT * FROM articlerecord WHERE databaseIndex BETWEEN :start AND :end")
    fun getInRange(start: Int, end: Int): List<ArticleRecord>

    @Query("SELECT * FROM articlerecord WHERE id = :id")
    fun getById(id: Int): ArticleRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(employee: ArticleRecord?)

    @Delete
    fun delete(employee: ArticleRecord?)

    @Query("DELETE FROM articlerecord")
    fun clear()
}

