package com.makentoshe.habrachan.application.android.di

import android.content.Context
import androidx.room.Room
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.ExceptionHandlerImpl
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.migration.AndroidCacheDatabaseMigration_1_2
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.userSession
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class ApplicationScope
class ApplicationModule(context: Context, cicerone: Cicerone<StackRouter>) : Module() {

    private val cacheDatabase = Room.databaseBuilder(
        context, AndroidCacheDatabase::class.java, "HabrachanCache"
    ).addMigrations(AndroidCacheDatabaseMigration_1_2()).fallbackToDestructiveMigration().build()

    init {
        bind<AndroidCacheDatabase>().toInstance(cacheDatabase)
        bind<StackRouter>().toInstance(cicerone.router)
        bind<Router>().toInstance(cicerone.router)
        bind<NavigatorHolder>().toInstance(cicerone.navigatorHolder)

        val userSession = userSession(BuildConfig.CLIENT_KEY, BuildConfig.API_KEY)
        bind<UserSession>().toInstance(userSession)

        bind<ExceptionHandler>().toInstance(ExceptionHandlerImpl(context))
    }
}