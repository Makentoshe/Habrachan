package com.makentoshe.habrachan.application.android.di

import android.content.Context
import androidx.room.Room
import com.makentoshe.habrachan.BuildConfig.API_KEY
import com.makentoshe.habrachan.BuildConfig.CLIENT_KEY
import com.makentoshe.habrachan.api.articles.ArticlesFilter
import com.makentoshe.habrachan.api.articles.DailyArticlesPeriod
import com.makentoshe.habrachan.api.articles.and
import com.makentoshe.habrachan.api.mobile.articles.*
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
import com.makentoshe.habrachan.application.android.database.record.UserSessionRecord
import com.makentoshe.habrachan.application.android.database.record.userdatabase.ArticlesFilterRecord
import com.makentoshe.habrachan.application.android.database.record.userdatabase.ArticlesUserSearchArticlesFilterCrossRef
import com.makentoshe.habrachan.application.android.database.record.userdatabase.ArticlesUserSearchRecord
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.toArticlesUserSearch
import com.makentoshe.habrachan.application.android.screen.login.navigation.LoginScreen
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

        val defaultUserSearches = userDatabase.articlesUserSearchDao().getAll().map { it.toArticlesUserSearch() }
        bind<Launcher>().toInstance(Launcher(LoginScreen()))
    }

    private fun initializeDefaultUserSearches() {
        if (userDatabase.articlesUserSearchDao().getAll().isNotEmpty()) return

        initializeDefaultAllArticlesUserSearchRecord()
        initializeDefaultMostReadingArticlesUserSearchRecord()
        initializeDefaultTopDailyArticlesUserSearchRecord()
    }

    private fun initializeDefaultAllArticlesUserSearchRecord() {
        val title = context.getString(R.string.articles_default_search_all)

        val articlesUserSearchRecord = ArticlesUserSearchRecord(title, true)
        userDatabase.articlesUserSearchDao().insert(articlesUserSearchRecord)

        (sortArticlesFilter(RatingArticlesSort) and pageArticlesFilter(1)).forEach { filter ->
            initializeArticlesFilterRecord(filter, articlesUserSearchRecord)
        }
    }

    private fun initializeArticlesFilterRecord(
        articlesFilter: ArticlesFilter,
        articlesUserSearchRecord: ArticlesUserSearchRecord
    ) {
        val record = ArticlesFilterRecord(articlesFilter.key, articlesFilter.value, articlesFilter.type)
        userDatabase.articlesFilterDao().insert(record)

        val crossRef = ArticlesUserSearchArticlesFilterCrossRef(articlesUserSearchRecord.title, record.keyValuePair)
        userDatabase.articlesUserSearchArticlesFilterCrossRef().insert(crossRef)
    }

    private fun initializeDefaultTopDailyArticlesUserSearchRecord() {
        val title = context.getString(R.string.articles_default_search_daily_top)

        val articlesUserSearchRecord = ArticlesUserSearchRecord(title, true)
        userDatabase.articlesUserSearchDao().insert(articlesUserSearchRecord)

        (sortArticlesFilter(DateArticlesSort) and periodArticlesFilter(DailyArticlesPeriod) and pageArticlesFilter(1)).forEach { filter ->
            initializeArticlesFilterRecord(filter, articlesUserSearchRecord)
        }
    }

    private fun initializeDefaultMostReadingArticlesUserSearchRecord() {
        val title = context.getString(R.string.articles_default_search_most_reading)

        val articlesUserSearchRecord = ArticlesUserSearchRecord(title, true)
        userDatabase.articlesUserSearchDao().insert(articlesUserSearchRecord)

        // Page uses only for successful storing in the cache
        (mostReadingArticlesFilter() and pageArticlesFilter(1)).forEach { filter ->
            initializeArticlesFilterRecord(filter, articlesUserSearchRecord)
        }
    }
}