package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.ArticleFlowCrossRef
import com.makentoshe.habrachan.application.android.database.record.ArticleHubCrossRef
import com.makentoshe.habrachan.application.android.database.record.ArticleRecord2
import com.makentoshe.habrachan.application.android.database.record.ArticleRecordWithHubAndFlowRecords

@Dao
interface ArticlesDao2 {

    @Transaction
    @Query("SELECT * FROM ArticleRecord2")
    fun getAll(): List<ArticleRecord2>

    @Query("SELECT * FROM ArticleRecord2 WHERE articleId = :id")
    fun getById(id: Int): ArticleRecord2?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: ArticleRecord2)

    /** Inserts a special associative entity */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleHubCrossRef: ArticleHubCrossRef)

    /** Inserts a special associative entity */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleFlowCrossRef: ArticleFlowCrossRef)

    @Transaction
    @Query("SELECT * FROM ArticleRecord2 WHERE articleId = :articleId")
    fun getByIdWithHubsAndFlows(articleId: Int): ArticleRecordWithHubAndFlowRecords?

    @Delete
    fun delete(record: ArticleRecord2)

    @Query("DELETE FROM ArticleRecord2")
    fun clear()

    @Transaction
    @Query("SELECT * FROM ArticleRecord2 ORDER BY timePublishedRaw DESC LIMIT :count OFFSET :offset")
    fun getTimePublishedDescSorted(offset: Int, count: Int): List<ArticleRecordWithHubAndFlowRecords>
}