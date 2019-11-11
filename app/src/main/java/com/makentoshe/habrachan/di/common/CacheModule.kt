package com.makentoshe.habrachan.di.common

import android.content.Context
import androidx.room.Room
import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.common.database.DataDao
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class CacheScope

class CacheModule(context: Context) : Module() {

    private val database = Room.databaseBuilder(context, HabrDatabase::class.java, "Habrachan").build()

    init {
        bind<DataDao>().toInstance(database.posts())
    }
}

