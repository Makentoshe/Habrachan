package com.makentoshe.habrachan.common.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.makentoshe.habrachan.common.entity.comment.Comment

@Dao
interface CommentDao {

    @Query("SELECT * FROM comment")
    fun getAll(): List<Comment>

    @Query("SELECT * FROM comment WHERE articleId = :articleId")
    fun getByArticleId(articleId: Int): List<Comment>

    @Query("SELECT * FROM comment WHERE id = :commentId")
    fun getById(commentId: Int): Comment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: Comment)

    @Query("DELETE FROM comment")
    fun clear()
}

