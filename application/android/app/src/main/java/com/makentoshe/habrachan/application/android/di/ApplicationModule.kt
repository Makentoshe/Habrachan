package com.makentoshe.habrachan.application.android.di

import android.content.Context
import androidx.room.Room
import com.makentoshe.habrachan.BuildConfig.API_KEY
import com.makentoshe.habrachan.BuildConfig.CLIENT_KEY
import com.makentoshe.habrachan.application.android.AndroidUserSession
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.ExceptionHandlerImpl
import com.makentoshe.habrachan.application.android.Launcher
import com.makentoshe.habrachan.application.android.common.AndroidUserSession2
import com.makentoshe.habrachan.application.android.common.R
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.UserSessionDatabase
import com.makentoshe.habrachan.application.android.database.migration.AndroidCacheDatabaseMigration_1_2
import com.makentoshe.habrachan.application.android.database.migration.userdatabase.userDatabaseMigrations
import com.makentoshe.habrachan.application.android.database.record.ArticlesUserSearchRecord
import com.makentoshe.habrachan.application.android.database.record.UserSessionRecord
import com.makentoshe.habrachan.application.android.screen.articles.flow.navigation.ArticlesFlowScreen
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.userSession
import toothpick.config.Module
import toothpick.ktp.binding.bind

class ApplicationModule(private val context: Context) : Module() {

    private val cacheDatabase = Room.databaseBuilder(
        context, AndroidCacheDatabase::class.java, "HabrachanCache"
    ).addMigrations(AndroidCacheDatabaseMigration_1_2()).fallbackToDestructiveMigration().build()

    private val userDatabase = Room.databaseBuilder(
        context, UserSessionDatabase::class.java, "HabrachanUserSensitive"
    ).allowMainThreadQueries().addMigrations(*userDatabaseMigrations).build()

    init {
        bind<AndroidCacheDatabase>().toInstance(cacheDatabase)
        bind<UserSessionDatabase>().toInstance(userDatabase)
        bind<Launcher>().toInstance(Launcher(ArticlesFlowScreen()))

        bind<ExceptionHandler>().toInstance(ExceptionHandlerImpl(context))

        if (userDatabase.userSessionDao().getAll().isEmpty()) {
            val record = UserSessionRecord(userSession(CLIENT_KEY, API_KEY), user = null)
            userDatabase.userSessionDao().insert(record)
        }

        val androidUserSession = AndroidUserSession(userDatabase.userSessionDao())
        bind<UserSession>().toInstance(androidUserSession)
        bind<AndroidUserSession>().toInstance(androidUserSession)

        val androidUserSession2 = AndroidUserSession2(userDatabase.userSessionDao())
        bind<AndroidUserSession2>().toInstance(androidUserSession2)

        initializeDefaultUserSearches()
    }

    private fun initializeDefaultUserSearches() {
        if (userDatabase.articlesUserSearchDao().getAll().isNotEmpty()) return

        initializeDefaultAllArticlesUserSearchRecord()
        initializeDefaultInterestingArticlesUserSearchRecord()
        initializeDefaultTopDailyArticlesUserSearchRecord()
    }

    private fun initializeDefaultAllArticlesUserSearchRecord() {
        val title = context.getString(R.string.articles_default_search_all)
        val params = mapOf("a.path" to "posts/all", "a.page" to "1")
        userDatabase.articlesUserSearchDao().insert(ArticlesUserSearchRecord(title, params, true))
    }

    private fun initializeDefaultInterestingArticlesUserSearchRecord() {
        val title = context.getString(R.string.articles_default_search_interesting)
        val params = mapOf("a.path" to "posts/interesting", "a.page" to "1")
        userDatabase.articlesUserSearchDao().insert(ArticlesUserSearchRecord(title, params, true))
    }

    private fun initializeDefaultTopDailyArticlesUserSearchRecord() {
        val title = context.getString(R.string.articles_default_search_daily_top)
        val params = mapOf("a.path" to "top/daily", "a.page" to "1")
        userDatabase.articlesUserSearchDao().insert(ArticlesUserSearchRecord(title, params, true))
    }
}