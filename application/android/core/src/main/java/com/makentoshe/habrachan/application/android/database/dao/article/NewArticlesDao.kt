package com.makentoshe.habrachan.application.android.database.dao.article

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.dao.article.ArticlesDao
import com.makentoshe.habrachan.application.android.database.record.article.NewArticleRecord

@Dao
interface NewArticlesDao: ArticlesDao<NewArticleRecord> {

    override val title: String
        get() = "NEW"

    @Query("SELECT * FROM NewArticleRecord")
    override fun getAll(): List<NewArticleRecord>

    @Query("SELECT * FROM NewArticleRecord ORDER BY timePublishedRaw DESC LIMIT :count OFFSET :offset")
    override fun getTimePublishedDescSorted(offset: Int, count: Int): List<NewArticleRecord>

    @Query("SELECT * FROM NewArticleRecord WHERE id = :id")
    override fun getById(id: Int): NewArticleRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(employee: NewArticleRecord)

    @Delete
    override fun delete(employee: NewArticleRecord)

    @Query("DELETE FROM NewArticleRecord")
    override fun clear()
}