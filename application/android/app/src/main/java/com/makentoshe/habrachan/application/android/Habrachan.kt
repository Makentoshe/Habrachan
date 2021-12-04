package com.makentoshe.habrachan.application.android

import android.app.Application
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.BuildVersionProviderImpl
import com.makentoshe.habrachan.application.android.di.*
import com.makentoshe.habrachan.application.android.screen.article.di.ArticleFragmentInjector
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesFlowFragmentInjector
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesPageFragmentInjector
import com.makentoshe.habrachan.application.android.screen.articles.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.comments.articles.di.ArticleCommentsFragmentInjector
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragmentInjector
import com.makentoshe.habrachan.application.android.screen.comments.thread.di.ThreadCommentsFragmentInjector
import com.makentoshe.habrachan.application.android.screen.content.di.ContentFragmentInjector
import com.makentoshe.habrachan.application.android.screen.content.di.ContentFragmentPageInjector
import com.makentoshe.habrachan.application.android.screen.login.di.LoginFragmentInjector
import com.makentoshe.habrachan.application.android.screen.main.di.MainFlowFragmentInjector
import com.makentoshe.habrachan.application.android.screen.user.di.UserFragmentInjector
import com.makentoshe.habrachan.application.android.screen.user.di.module.MeUserNetworkModule
import ru.terrakok.cicerone.Cicerone
import toothpick.Toothpick
import toothpick.configuration.Configuration

class Habrachan : Application() {

    companion object : Analytics(LogAnalytic())

    private val injectActivityLifecycleCallback = AppActivityInjector(
        MainFlowFragmentInjector(),
        ArticlesFlowFragmentInjector(),
        ArticlesPageFragmentInjector(),
        ArticleFragmentInjector(),
        ContentFragmentInjector(),
        ContentFragmentPageInjector(),
        LoginFragmentInjector(),
        UserFragmentInjector(),
        ArticleCommentsFragmentInjector(),
        ThreadCommentsFragmentInjector(),
//        CommentDetailsFragmentInjector(),
        DispatchCommentsFragmentInjector(),
    )

    private val buildVersionProvider = BuildVersionProviderImpl()

    override fun onCreate() {
        super.onCreate()
        Toothpick.setConfiguration(getToothpickConfiguration())

        LogAnalytic.Factory.registry(BuildConfig.DEBUG)

        val applicationModule = ApplicationModule(this)
        val navigationModule = NavigationModule(Cicerone.create(StackRouter()))
        val networkModule = NetworkModule()
        val userModule = UserModule(applicationContext, buildVersionProvider)

        val scopes = Toothpick.openScopes(ApplicationScope::class)
        scopes.installModules(
            applicationModule, userModule, navigationModule, networkModule,
            MeUserNetworkModule(this)
        ).inject(this)

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
