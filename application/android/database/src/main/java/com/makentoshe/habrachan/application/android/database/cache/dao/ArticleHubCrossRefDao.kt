package com.makentoshe.habrachan.application.android.database.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.application.android.database.cache.record.ArticleHubCrossRef

@Dao
interface ArticleHubCrossRefDao {

    @Query("SELECT * FROM ArticleHubCrossRef")
    fun getAll(): List<ArticleHubCrossRef>

    /** Inserts a special associative entity */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleHubCrossRef: ArticleHubCrossRef)

    @Query("SELECT * FROM ArticleHubCrossRef WHERE articleId = :articleId")
    fun getByArticleId(articleId: Int): List<ArticleHubCrossRef>

    @Query("SELECT * FROM ArticleHubCrossRef WHERE hubId = :hubId")
    fun getByHubId(hubId: Int): List<ArticleHubCrossRef>

    @Query("DELETE FROM ArticleHubCrossRef WHERE articleId = :articleId")
    fun clearByArticleId(articleId: Int)

    @Query("DELETE FROM ArticleHubCrossRef WHERE hubId= :hubId")
    fun clearByHubId(hubId: Int)
}