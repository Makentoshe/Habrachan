package com.makentoshe.habrachan.application.android.database.dao

import androidx.room.*
import com.makentoshe.habrachan.application.android.database.record.CommentRecord

@Dao
interface CommentDao {

    @Query("SELECT * FROM CommentRecord")
    fun getAll(): List<CommentRecord>

    @Query("SELECT * FROM CommentRecord WHERE id = :id")
    fun getByCommentId(id: Int): CommentRecord?

    @Query("SELECT * FROM CommentRecord WHERE articleId=:id")
    fun getByArticleId(id: Int): List<CommentRecord>

    @Query("SELECT COUNT(*) FROM CommentRecord")
    fun count(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: CommentRecord)

    @Delete
    fun delete(record: CommentRecord)

    @Query("DELETE FROM FlowRecord")
    fun clear()

}