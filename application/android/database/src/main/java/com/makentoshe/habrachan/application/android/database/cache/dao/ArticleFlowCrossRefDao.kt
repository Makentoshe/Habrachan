package com.makentoshe.habrachan.application.android.database.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.application.android.database.cache.record.ArticleFlowCrossRef

@Dao
interface ArticleFlowCrossRefDao {

    @Query("SELECT * FROM ArticleFlowCrossRef")
    fun getAll(): List<ArticleFlowCrossRef>

    /** Inserts a special associative entity */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleFlowCrossRef: ArticleFlowCrossRef)

    @Query("DELETE FROM ArticleFlowCrossRef WHERE articleId = :articleId")
    fun clearByArticleId(articleId: Int)
}