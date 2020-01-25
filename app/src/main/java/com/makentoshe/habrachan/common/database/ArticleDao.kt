package com.makentoshe.habrachan.common.database

import androidx.room.*
import com.makentoshe.habrachan.common.entity.Article

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article")
    fun getAll(): List<Article>

    @Query("SELECT * FROM article WHERE id = :id")
    fun getById(id: Int): Article?

    @Query("SELECT * FROM article WHERE `index` = :index")
    fun getByIndex(index: Int): Article?

    @Delete
    fun delete(article: Article)

    @Query("DELETE FROM article")
    fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article)
}
