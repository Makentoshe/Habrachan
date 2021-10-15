package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.ArticleHubRecord3

@Dao
interface HubDao3 {

    @Query("SELECT * FROM ArticleHubRecord3")
    fun getAll(): List<ArticleHubRecord3>

    @Query("SELECT * FROM ArticleHubRecord3 WHERE hubId = :hubId")
    fun getByHubId(hubId: Int): ArticleHubRecord3?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: ArticleHubRecord3)

    @Delete
    fun delete(record: ArticleHubRecord3)

    @Query("DELETE FROM ARTICLEHUBRECORD3")
    fun clear()
}