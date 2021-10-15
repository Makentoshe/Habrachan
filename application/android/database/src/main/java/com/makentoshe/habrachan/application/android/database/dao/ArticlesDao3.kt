package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.ArticleRecord3

@Dao
interface ArticlesDao3 {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: ArticleRecord3)

    @Delete
    fun delete(record: ArticleRecord3)

    @Query("SELECT * FROM ArticleRecord3 WHERE articleId = :id")
    fun getById(id: Int): ArticleRecord3?


    @Transaction
    @Query("SELECT * FROM ArticleRecord3")
    fun getAll(): List<ArticleRecord3>

    /** Return articles marked with the [type] */
    @Query("SELECT * FROM ArticleRecord3 WHERE internalType = :type")
    fun getAll(type: String): List<ArticleRecord3>


    @Query("DELETE FROM ArticleRecord3")
    fun clear()

    /** Remove articles marked with the [type] */
    @Query("DELETE FROM ArticleRecord3 WHERE internalType = :type")
    fun clear(type: String)


    /** Return articles marked with the [type] */
    @Transaction
    @Query("SELECT * FROM ArticleRecord3 WHERE internalType = :type ORDER BY timePublished DESC LIMIT :count OFFSET :offset")
    fun getDescSortedByTimePublished(offset: Int, count: Int, type: String): List<ArticleRecord3>
}