package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.application.android.database.record.ArticleHubCrossRef
import com.makentoshe.habrachan.entity.ArticleId

@Dao
interface ArticleHubCrossRefDao {

    @Query("SELECT * FROM ArticleHubCrossRef")
    fun getAll(): List<ArticleHubCrossRef>

    /** Inserts a special associative entity */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleHubCrossRef: ArticleHubCrossRef)

    @Query("DELETE FROM ArticleHubCrossRef WHERE articleId = :articleId")
    fun clearByArticleId(articleId: Int)
}