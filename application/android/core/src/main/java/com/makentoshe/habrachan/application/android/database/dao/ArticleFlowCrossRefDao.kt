package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.application.android.database.record.ArticleFlowCrossRef
import com.makentoshe.habrachan.application.android.database.record.ArticleHubCrossRef
import com.makentoshe.habrachan.entity.ArticleId

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