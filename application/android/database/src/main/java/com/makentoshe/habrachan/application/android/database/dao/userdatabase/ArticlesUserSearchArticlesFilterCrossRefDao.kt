package com.makentoshe.habrachan.application.android.database.dao.userdatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.application.android.database.record.userdatabase.ArticlesUserSearchArticlesFilterCrossRef

@Dao
interface ArticlesUserSearchArticlesFilterCrossRefDao {

    @Query("SELECT * FROM ArticlesUserSearchArticlesFilterCrossRef")
    fun getAll(): List<ArticlesUserSearchArticlesFilterCrossRef>

    /** Inserts a special associative entity */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleHubCrossRef: ArticlesUserSearchArticlesFilterCrossRef)

    @Query("SELECT * FROM ArticlesUserSearchArticlesFilterCrossRef WHERE title = :title")
    fun getByTitle(title: String): List<ArticlesUserSearchArticlesFilterCrossRef>

    @Query("SELECT * FROM ArticlesUserSearchArticlesFilterCrossRef WHERE keyValuePair = :pair")
    fun getByPair(pair: String): List<ArticlesUserSearchArticlesFilterCrossRef>

    @Query("DELETE FROM ArticlesUserSearchArticlesFilterCrossRef WHERE title = :title")
    fun clearByTitle(title: String)
}