package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.ArticleRecord2
import com.makentoshe.habrachan.application.android.database.record.InterestingArticleRecord
import com.makentoshe.habrachan.application.android.database.record.NewArticleRecord
import com.makentoshe.habrachan.application.android.database.record.TopArticleRecord

interface ArticlesDao2<T: ArticleRecord2> {

    val title: String

    fun getAll(): List<T>

    fun getById(id: Int): T?

    fun getTimePublishedDescSorted(offset: Int, count: Int): List<T>

    fun insert(employee: T)

    fun delete(employee: T)

    fun clear()
}

@Dao
interface NewArticlesDao: ArticlesDao2<NewArticleRecord> {

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
@Dao
interface InterestingArticlesDao: ArticlesDao2<InterestingArticleRecord> {

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

@Dao
interface TopArticlesDao: ArticlesDao2<TopArticleRecord> {

    override val title: String
        get() = "TOP"

    @Query("SELECT * FROM TopArticleRecord")
    override fun getAll(): List<TopArticleRecord>

    @Query("SELECT * FROM TopArticleRecord ORDER BY timePublishedRaw DESC LIMIT :count OFFSET :offset")
    override fun getTimePublishedDescSorted(offset: Int, count: Int): List<TopArticleRecord>

    @Query("SELECT * FROM TopArticleRecord WHERE id = :id")
    override fun getById(id: Int): TopArticleRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override fun insert(employee: TopArticleRecord)

    @Delete
    override fun delete(employee: TopArticleRecord)

    @Query("DELETE FROM TopArticleRecord")
    override fun clear()
}
