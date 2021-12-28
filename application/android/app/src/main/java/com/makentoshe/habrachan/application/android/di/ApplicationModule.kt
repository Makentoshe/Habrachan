package com.makentoshe.habrachan.application.android.di

import android.app.Application
import androidx.room.Room
import com.makentoshe.habrachan.BuildConfig.API_KEY
import com.makentoshe.habrachan.BuildConfig.CLIENT_KEY
import com.makentoshe.habrachan.api.articles.ArticlesFilter
import com.makentoshe.habrachan.api.articles.DailyArticlesPeriod
import com.makentoshe.habrachan.api.articles.and
import com.makentoshe.habrachan.api.mobile.articles.*
import com.makentoshe.habrachan.application.android.*
import com.makentoshe.habrachan.application.android.common.AndroidUserSession2
import com.makentoshe.habrachan.application.android.common.R
import com.makentoshe.habrachan.application.android.common.strings.BundledStringsProvider
import com.makentoshe.habrachan.application.android.common.strings.StringsProvider
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.cache.migration.AndroidCacheDatabaseMigration_1_2
import com.makentoshe.habrachan.application.android.database.cache.record.UserSessionRecord
import com.makentoshe.habrachan.application.android.database.user.UserSessionDatabase
import com.makentoshe.habrachan.application.android.database.user.migration.userDatabaseMigrations
import com.makentoshe.habrachan.application.android.database.user.record.ArticlesFilterRecord
import com.makentoshe.habrachan.application.android.database.user.record.ArticlesUserSearchArticlesFilterCrossRef
import com.makentoshe.habrachan.application.android.database.user.record.ArticlesUserSearchRecord
import com.makentoshe.habrachan.application.android.screen.articles.model.toArticlesUserSearch
import com.makentoshe.habrachan.application.android.screen.articles.navigation.ArticlesFlowScreen
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.userSession
import toothpick.config.Module
import toothpick.ktp.binding.bind
import java.io.File

class ApplicationModule(private val application: Habrachan) : Module() {

    private val cacheDatabase = Room.databaseBuilder(
        application, AndroidCacheDatabase::class.java, "HabrachanCache"
    ).addMigrations(AndroidCacheDatabaseMigration_1_2()).fallbackToDestructiveMigration().build()

    private val userDatabase = Room.databaseBuilder(
        application, UserSessionDatabase::class.java, "HabrachanUserSensitive"
    ).allowMainThreadQueries().addMigrations(*userDatabaseMigrations).build()

    init {
        bind<Application>().toInstance(application)
        bind<AndroidCacheDatabase>().toInstance(cacheDatabase)
        bind<UserSessionDatabase>().toInstance(userDatabase)

        val stringsProvider = BundledStringsProvider(application)
        bind<BundledStringsProvider>().toInstance(stringsProvider)
        bind<StringsProvider<Int>>().toInstance(stringsProvider)

        bind<File>().toInstance(application.cacheDir)

        bind<ExceptionHandler>().toInstance(ExceptionHandlerImpl(application))

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
        val defaultScreen = ArticlesFlowScreen(defaultUserSearches)
        bind<Launcher>().toInstance(Launcher(defaultScreen))
    }

    private fun initializeDefaultUserSearches() {
        if (userDatabase.articlesUserSearchDao().getAll().isNotEmpty()) return

        initializeDefaultAllArticlesUserSearchRecord()
        initializeDefaultMostReadingArticlesUserSearchRecord()
        initializeDefaultTopDailyArticlesUserSearchRecord()
    }

    private fun initializeDefaultAllArticlesUserSearchRecord() {
        val title = application.getString(R.string.articles_default_search_all)

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
        val title = application.getString(R.string.articles_default_search_daily_top)

        val articlesUserSearchRecord = ArticlesUserSearchRecord(title, true)
        userDatabase.articlesUserSearchDao().insert(articlesUserSearchRecord)

        (sortArticlesFilter(DateArticlesSort) and periodArticlesFilter(DailyArticlesPeriod) and pageArticlesFilter(1)).forEach { filter ->
            initializeArticlesFilterRecord(filter, articlesUserSearchRecord)
        }
    }

    private fun initializeDefaultMostReadingArticlesUserSearchRecord() {
        val title = application.getString(R.string.articles_default_search_most_reading)

        val articlesUserSearchRecord = ArticlesUserSearchRecord(title, true)
        userDatabase.articlesUserSearchDao().insert(articlesUserSearchRecord)

        // Page uses only for successful storing in the cache
        (mostReadingArticlesFilter() and pageArticlesFilter(1)).forEach { filter ->
            initializeArticlesFilterRecord(filter, articlesUserSearchRecord)
        }
    }
}