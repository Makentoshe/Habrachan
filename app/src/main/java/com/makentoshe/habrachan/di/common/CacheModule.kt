package com.makentoshe.habrachan.di.common

import android.content.Context
import androidx.room.Room
import com.makentoshe.habrachan.common.database.*
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class CacheScope

class CacheModule(context: Context) : Module() {

    private val database = Room.databaseBuilder(context, HabrDatabase::class.java, "Habrachan").build()

    private val imageDatabase = ImageDatabase(context)

    init {
        bind<ArticleDao>().toInstance(database.articles())
        bind<CommentDao>().toInstance(database.comments())
        bind<AvatarDao>().toInstance(imageDatabase.avatars())
    }
}

