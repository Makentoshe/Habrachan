package com.makentoshe.habrachan.application.android.di

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.room.Room
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.ExceptionHandlerImpl
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.migration.AndroidCacheDatabaseMigration12
import com.makentoshe.habrachan.application.android.network.AndroidUserSession
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.SchedulersProvider
import com.makentoshe.habrachan.application.android.viewmodel.ExecutorsProvider
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module
import toothpick.ktp.binding.bind
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.net.ssl.HostnameVerifier

annotation class ApplicationScope

class ApplicationModule(context: Context, cicerone: Cicerone<Router>) : Module() {

    private val client = OkHttpClient.Builder().followRedirects(true).addLoggingInterceptor().build()

    private val cacheDatabase = Room.databaseBuilder(
        context, AndroidCacheDatabase::class.java, "HabrachanCache"
    ).addMigrations(AndroidCacheDatabaseMigration12()).build()

    private val executorsProvider = object : ExecutorsProvider {
        override val fetchExecutor = Executors.newCachedThreadPool()
        override val notifyExecutor = Executor { Handler(Looper.getMainLooper()).post(it) }
    }

    private val schedulersProvider = object : SchedulersProvider {
        override val ioScheduler = Schedulers.io()
    }

    init {
        bind<OkHttpClient>().toInstance(client)
        bind<AndroidCacheDatabase>().toInstance(cacheDatabase)
        bind<Router>().toInstance(cicerone.router)
        bind<NavigatorHolder>().toInstance(cicerone.navigatorHolder)
        bind<ExecutorsProvider>().toInstance(executorsProvider)
        bind<SchedulersProvider>().toInstance(schedulersProvider)

        val articlesRequestSpec = GetArticlesRequest.Spec.All(include = "text_html")
        val userSession = AndroidUserSession(BuildConfig.CLIENT_KEY, BuildConfig.API_KEY, null, articlesRequestSpec)
        bind<UserSession>().toInstance(userSession)

        bind<ExceptionHandler>().toInstance(ExceptionHandlerImpl(context))
    }

    private fun OkHttpClient.Builder.addLoggingInterceptor(): OkHttpClient.Builder {
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            addInterceptor(logging)
        }
        return this
    }


    /**
     * Requires for oauth
     * todo: make xml certificate?
     */
    private fun OkHttpClient.Builder.addHostnameVerifier(): OkHttpClient.Builder {
        val hostnameVerifier = HostnameVerifier { hostname, _ ->
            hostname == "habr.com" || hostname == "account.habr.com" || hostname == "github.com"
        }
        this.hostnameVerifier(hostnameVerifier)
        return this
    }
}