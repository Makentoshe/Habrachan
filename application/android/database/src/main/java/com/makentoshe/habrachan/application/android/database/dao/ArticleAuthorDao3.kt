package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.ArticleAuthorRecord3

@Dao
interface ArticleAuthorDao3 {

    @Query("SELECT * FROM ArticleAuthorRecord3")
    fun getAll(): List<ArticleAuthorRecord3>

    @Query("SELECT * FROM ArticleAuthorRecord3 WHERE authorId = :authorId")
    fun getById(authorId: Int): ArticleAuthorRecord3?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: ArticleAuthorRecord3)

    @Delete
    fun delete(record: ArticleAuthorRecord3)

    @Query("DELETE FROM ArticleAuthorRecord3")
    fun clear()
}