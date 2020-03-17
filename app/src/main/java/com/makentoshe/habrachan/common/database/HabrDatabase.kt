package com.makentoshe.habrachan.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.entity.session.UserSession

@Database(entities = [Article::class, Comment::class, UserSession::class, User::class], version = 1)
@TypeConverters(Converters::class)
abstract class HabrDatabase : RoomDatabase() {

    abstract fun articles(): ArticleDao

    abstract fun comments(): CommentDao

    abstract fun session(): SessionDao

    abstract fun users() : UserDao
}