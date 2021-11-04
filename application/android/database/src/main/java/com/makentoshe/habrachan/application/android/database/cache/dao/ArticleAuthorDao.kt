package com.makentoshe.habrachan.application.android.database.cache.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.cache.record.ArticleAuthorRecord

@Dao
interface ArticleAuthorDao {

    @Query("SELECT * FROM ArticleAuthorRecord")
    fun getAll(): List<ArticleAuthorRecord>

    @Query("SELECT * FROM ArticleAuthorRecord WHERE userId = :id")
    fun getById(id: Int): ArticleAuthorRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: ArticleAuthorRecord)

    @Delete
    fun delete(record: ArticleAuthorRecord)

    @Query("DELETE FROM ArticleAuthorRecord")
    fun clear()
}