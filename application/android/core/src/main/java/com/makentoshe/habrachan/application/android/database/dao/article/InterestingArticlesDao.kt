package com.makentoshe.habrachan.application.android.database.dao.article

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.dao.article.ArticlesDao
import com.makentoshe.habrachan.application.android.database.record.article.InterestingArticleRecord

@Dao
interface InterestingArticlesDao: ArticlesDao<InterestingArticleRecord> {

    override val title: String
        get() = "INTERESTING"

    @Query("SELECT * FROM InterestingArticleRecord")
    override fun getAll(): List<InterestingArticleRecord>

    @Query("SELECT * FROM InterestingArticleRecord ORDER BY timePublishedRaw DESC LIMIT :count OFFSET :offset")
    override fun getTimePublishedDescSorted(offset: Int, count: Int): List<InterestingArticleRecord>

    @Query("SELECT * FROM InterestingArticleRecord WHERE id = :id")
    override fun getById(id: Int): InterestingArticleRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(employee: InterestingArticleRecord)

    @Delete
    override fun delete(employee: InterestingArticleRecord)

    @Query("DELETE FROM InterestingArticleRecord")
    override fun clear()
}