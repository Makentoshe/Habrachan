package com.makentoshe.habrachan.application.android.database.dao.article

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.dao.article.ArticlesDao
import com.makentoshe.habrachan.application.android.database.record.article.TempArticleRecord

@Dao
interface TempArticlesDao: ArticlesDao<TempArticleRecord> {

    override val title: String
        get() = "TEMP"

    @Query("SELECT * FROM TempArticleRecord")
    override fun getAll(): List<TempArticleRecord>

    @Query("SELECT * FROM TempArticleRecord ORDER BY timePublishedRaw DESC LIMIT :count OFFSET :offset")
    override fun getTimePublishedDescSorted(offset: Int, count: Int): List<TempArticleRecord>

    @Query("SELECT * FROM TopArticleRecord WHERE id = :id")
    override fun getById(id: Int): TempArticleRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(employee: TempArticleRecord)

    @Delete
    override fun delete(employee: TempArticleRecord)

    @Query("DELETE FROM TempArticleRecord")
    override fun clear()
}