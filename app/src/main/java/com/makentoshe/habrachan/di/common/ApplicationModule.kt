package com.makentoshe.habrachan.di.common

import android.content.Context
import androidx.room.Room
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.common.entity.session.UserSession
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module
import toothpick.ktp.binding.bind

class ApplicationModule(context: Context) : Module() {

    private val client = OkHttpClient.Builder().addLoggingInterceptor().build()

    private val database = Room.databaseBuilder(
        context, HabrDatabase::class.java, "Habrachan"
    ).allowMainThreadQueries().build()

    init {
        bind<OkHttpClient>().toInstance(client)
        bind<HabrDatabase>().toInstance(database)

        if (database.session().get() == null) {
            val session = UserSession(BuildConfig.CLIENT_KEY, BuildConfig.API_KEY)
            database.session().insert(session)
        }
    }

    private fun OkHttpClient.Builder.addLoggingInterceptor(): OkHttpClient.Builder {
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            addInterceptor(logging)
        }
        return this
    }
}