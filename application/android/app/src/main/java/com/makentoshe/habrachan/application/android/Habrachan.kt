package com.makentoshe.habrachan.application.android

import android.app.Application
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.di.AppActivityInjector
import com.makentoshe.habrachan.application.android.di.ApplicationModule
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.di.NetworkModule
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.article.di.ArticleFragmentInjector
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesFlowFragmentInjector
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesFragmentInjector
import com.makentoshe.habrachan.application.android.screen.comments.di.ArticleCommentsFragmentInjector
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentDetailsFragmentModule
import com.makentoshe.habrachan.application.android.screen.comments.di.DiscussionCommentsFragmentInjector
import com.makentoshe.habrachan.application.android.screen.content.di.ContentFragmentInjector
import com.makentoshe.habrachan.application.android.screen.content.di.ContentFragmentPageInjector
import com.makentoshe.habrachan.application.android.screen.login.di.LoginFragmentInjector
import com.makentoshe.habrachan.application.android.screen.main.di.MainFlowFragmentInjector
import com.makentoshe.habrachan.application.android.screen.user.di.UserFragmentInjector
import ru.terrakok.cicerone.Cicerone
import toothpick.Toothpick
import toothpick.configuration.Configuration

class Habrachan : Application() {

    companion object : Analytics(LogAnalytic())

    private val injectActivityLifecycleCallback = AppActivityInjector(
        MainFlowFragmentInjector(),
        ArticlesFlowFragmentInjector(),
        ArticlesFragmentInjector(),
        ArticleFragmentInjector(),
        ContentFragmentInjector(),
        ContentFragmentPageInjector(),
        LoginFragmentInjector(),
        UserFragmentInjector(),
        ArticleCommentsFragmentInjector(),
        DiscussionCommentsFragmentInjector(),
        CommentDetailsFragmentModule()
    )

    override fun onCreate() {
        super.onCreate()
        Toothpick.setConfiguration(getToothpickConfiguration())

        LogAnalytic.Factory.registry(BuildConfig.DEBUG)

        val applicationModule = ApplicationModule(applicationContext, Cicerone.create(StackRouter()))
        val networkModule = NetworkModule(applicationContext)

        val scopes = Toothpick.openScopes(ApplicationScope::class)
        scopes.installModules(applicationModule, networkModule).inject(this)

        registerActivityLifecycleCallbacks(injectActivityLifecycleCallback)

        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            capture(analyticEvent("UnhandledException", paramThrowable.toString()))
            throw paramThrowable
        }
    }

    private fun getToothpickConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
            Configuration.forDevelopment().preventMultipleRootScopes()
        } else {
            Configuration.forProduction()
        }
    }
}
