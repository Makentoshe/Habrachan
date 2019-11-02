package com.makentoshe.habrachan.di.common

import android.content.Context
import androidx.room.Room
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.cache.ImagesCache
import com.makentoshe.habrachan.common.cache.InMemoryCacheStorage
import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.common.database.PostsDao
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.model.main.posts.PostsCache
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class CacheScope

class CacheModule(context: Context) : Module() {

    private val postsCache = PostsCache(InMemoryCacheStorage())
    private val imagesCache = ImagesCache(InMemoryCacheStorage())

    init {
        bind<Cache<Int, Data>>().toInstance(postsCache)
        bind<ImagesCache>().toInstance(imagesCache)

        val database = Room.databaseBuilder(context, HabrDatabase::class.java, "Sas").build()
        bind<PostsDao>().toInstance(database.posts())
    }
}

