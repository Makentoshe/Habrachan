package com.makentoshe.habrachan.common.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.common.entity.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article WHERE id = :id")
    fun getById(id: Int): Article?

    /** Returns all articles marked with index column in ascending order */
    @Query("SELECT * FROM article WHERE searchIndex IS NOT NULL ORDER BY searchIndex")
    fun getAllSortedByDescendingIndex(): List<Article>

    /** Returns all articles where selected user is author */
    @Query("SELECT * FROM article WHERE author_login = :login ORDER BY timePublished DESC")
    fun getAllByUserLoginSortedByDescendingPublicationTime(login: String): List<Article>

    @Query("DELETE FROM article")
    fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article)
}
