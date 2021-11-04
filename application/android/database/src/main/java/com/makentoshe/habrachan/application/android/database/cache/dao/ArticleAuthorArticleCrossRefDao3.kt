package com.makentoshe.habrachan.application.android.database.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.application.android.database.cache.record.ArticleAuthorCrossRef

@Dao
interface ArticleAuthorArticleCrossRefDao3 {

    @Query("SELECT * FROM ArticleAuthorCrossRef")
    fun getAll(): List<ArticleAuthorCrossRef>

    /** Inserts a special associative entity */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleHubCrossRef: ArticleAuthorCrossRef)

    @Query("SELECT * FROM ArticleAuthorCrossRef WHERE articleAuthorId = :authorId")
    fun getByAuthorId(authorId: Int): List<ArticleAuthorCrossRef>

    @Query("SELECT * FROM ArticleAuthorCrossRef WHERE articleId = :articleId")
    fun getByArticleId(articleId: Int): ArticleAuthorCrossRef?

    @Query("DELETE FROM ArticleAuthorCrossRef WHERE articleId = :articleId")
    fun clearByArticleId(articleId: Int)

    @Query("DELETE FROM ArticleAuthorCrossRef WHERE articleAuthorId = :authorId")
    fun clearByAuthorId(authorId: Int)
}