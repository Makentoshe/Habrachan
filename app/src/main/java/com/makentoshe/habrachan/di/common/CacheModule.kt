package com.makentoshe.habrachan.di.common

import android.content.Context
import androidx.room.Room
import com.makentoshe.habrachan.BuildConfig
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
        bind<UserDao>().toInstance(database.users())

        val session = database.session().get()
        if (session == null) database.session().insert(
            UserSession(apiKey = BuildConfig.API_KEY, clientKey = BuildConfig.CLIENT_KEY)
        )
        bind<SessionDao>().toInstance(database.session())
    }

}

