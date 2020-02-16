package com.makentoshe.habrachan.di.common

import android.content.Context
import androidx.room.Room
import com.makentoshe.habrachan.common.database.*
import com.makentoshe.habrachan.common.entity.session.UserSession
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class CacheScope

class CacheModule(context: Context) : Module() {

    private val database = Room.databaseBuilder(context, HabrDatabase::class.java, "Habrachan")
        .allowMainThreadQueries().build()

    private val imageDatabase = ImageDatabase(context)

    init {
        bind<ArticleDao>().toInstance(database.articles())
        bind<CommentDao>().toInstance(database.comments())
        bind<AvatarDao>().toInstance(imageDatabase.avatars())

        val session = database.session().get()
        if (session == null) database.session().insert(
            UserSession(apiKey = "173984950848a2d27c0cc1c76ccf3d6d3dc8255b", clientKey = "85cab69095196f3.89453480")
        )
        bind<SessionDao>().toInstance(database.session())
    }

}

