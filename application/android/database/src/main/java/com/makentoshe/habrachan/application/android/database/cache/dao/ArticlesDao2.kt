package com.makentoshe.habrachan.application.android.database.cache.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.cache.record.ArticleRecord2
import com.makentoshe.habrachan.application.android.database.cache.record.ArticleRecordWithHubAndFlowRecords

@Dao
interface ArticlesDao2 {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: ArticleRecord2)

    @Delete
    fun delete(record: ArticleRecord2)

    @Query("SELECT * FROM ArticleRecord2 WHERE articleId = :id")
    fun getById(id: Int): ArticleRecord2?

    @Transaction
    @Query("SELECT * FROM ArticleRecord2 WHERE articleId = :articleId")
    fun getByIdWithHubsAndFlows(articleId: Int): ArticleRecordWithHubAndFlowRecords?


    @Transaction
    @Query("SELECT * FROM ArticleRecord2")
    fun getAll(): List<ArticleRecord2>

    @Query("DELETE FROM ArticleRecord2")
    fun clear()

    @Transaction
    @Query("SELECT * FROM ArticleRecord2 ORDER BY timePublishedRaw DESC LIMIT :count OFFSET :offset")
    fun getTimePublishedDescSorted(offset: Int, count: Int): List<ArticleRecordWithHubAndFlowRecords>


    /** Return articles marked with the [type] */
    @Query("SELECT * FROM ArticleRecord2 WHERE type = :type")
    fun getAll(type: String): List<ArticleRecord2>

    /** Return articles marked with the [type] */
    @Transaction
    @Query("SELECT * FROM ArticleRecord2 WHERE type = :type ORDER BY timePublishedRaw DESC LIMIT :count OFFSET :offset")
    fun getTimePublishedDescSorted(offset: Int, count: Int, type: String): List<ArticleRecordWithHubAndFlowRecords>

    /** Remove articles marked with the [type] */
    @Query("DELETE FROM ArticleRecord2 WHERE type = :type")
    fun clear(type: String)
}