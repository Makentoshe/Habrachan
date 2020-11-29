package com.makentoshe.habrachan.application.android.di

import android.content.Context
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module
import toothpick.ktp.binding.bind
import javax.inject.Qualifier
import javax.net.ssl.HostnameVerifier

@Qualifier
annotation class ApplicationScope

class ApplicationModule(context: Context, cicerone: Cicerone<Router>) : Module() {

    private val client = OkHttpClient.Builder().followRedirects(false).build()//.addLoggingInterceptor().build()

    init {
        bind<OkHttpClient>().toInstance(client)
        bind<Router>().toInstance(cicerone.router)
        bind<NavigatorHolder>().toInstance(cicerone.navigatorHolder)
    }

//    private fun OkHttpClient.Builder.addLoggingInterceptor(): OkHttpClient.Builder {
//        if (BuildConfig.DEBUG) {
//            val logging = HttpLoggingInterceptor()
//            logging.level = HttpLoggingInterceptor.Level.BASIC
//            addInterceptor(logging)
//        }
//        return this
//    }


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