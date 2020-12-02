package com.makentoshe.habrachan.di.common

import android.content.Context
import androidx.room.Room
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module
import toothpick.ktp.binding.bind
import javax.net.ssl.HostnameVerifier

annotation class ApplicationScope

class ApplicationModule(context: Context, cicerone: Cicerone<Router>) : Module() {

    private val client =
        OkHttpClient.Builder().followRedirects(false).addLoggingInterceptor().build()

    private val cacheDatabase = Room.databaseBuilder(
        context, AndroidCacheDatabase::class.java, "HabrachanCache"
    ).build()

    init {
        bind<OkHttpClient>().toInstance(client)
        bind<AndroidCacheDatabase>().toInstance(cacheDatabase)
        bind<Router>().toInstance(cicerone.router)
        bind<NavigatorHolder>().toInstance(cicerone.navigatorHolder)
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