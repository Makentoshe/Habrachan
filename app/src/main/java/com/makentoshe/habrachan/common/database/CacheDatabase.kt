package com.makentoshe.habrachan.common.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.common.entity.comment.Comment

@Database(entities = [Article::class, Comment::class, User::class], version = 1)
@TypeConverters(Converters::class)
abstract class CacheDatabase : RoomDatabase() {

    /** Do not use this context */
    lateinit var context: Context

    abstract fun articles(): ArticleDao

    abstract fun comments(): CommentDao

    abstract fun users() : UserDao

    fun avatars() = AvatarDao(context)
}