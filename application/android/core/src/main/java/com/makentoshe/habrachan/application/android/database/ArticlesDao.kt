package com.makentoshe.habrachan.application.android.database

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.ArticleRecord

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM ArticleRecord")
    fun getAll(): List<ArticleRecord>

    @Query("SELECT * FROM ArticleRecord ORDER BY timePublishedRaw DESC LIMIT :count OFFSET :offset")
    fun getTimePublishedDescSorted(offset: Int, count: Int): List<ArticleRecord>

    @Query("SELECT * FROM ArticleRecord WHERE id = :id")
    fun getById(id: Int): ArticleRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(employee: ArticleRecord)

    @Delete
    fun delete(employee: ArticleRecord)

    @Query("DELETE FROM ArticleRecord")
    fun clear()
}

