package com.makentoshe.habrachan.application.android.database.dao.article

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.dao.article.ArticlesDao
import com.makentoshe.habrachan.application.android.database.record.article.TopArticleRecord

@Dao
interface TopArticlesDao: ArticlesDao<TopArticleRecord> {

    override val title: String
        get() = "TOP"

    @Query("SELECT * FROM TopArticleRecord")
    override fun getAll(): List<TopArticleRecord>

    @Query("SELECT * FROM TopArticleRecord ORDER BY timePublishedRaw DESC LIMIT :count OFFSET :offset")
    override fun getTimePublishedDescSorted(offset: Int, count: Int): List<TopArticleRecord>

    @Query("SELECT * FROM TopArticleRecord WHERE articleId = :id")
    override fun getById(id: Int): TopArticleRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(employee: TopArticleRecord)

    @Delete
    override fun delete(employee: TopArticleRecord)

    @Query("DELETE FROM TopArticleRecord")
    override fun clear()
}