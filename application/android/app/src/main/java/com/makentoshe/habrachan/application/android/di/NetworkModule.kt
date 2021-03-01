package com.makentoshe.habrachan.application.android.di

import android.content.Context
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.network.manager.GetContentManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module
import toothpick.ktp.binding.bind
import javax.net.ssl.HostnameVerifier

class NetworkModule(context: Context): Module() {

    private val client = OkHttpClient.Builder().followRedirects(true).addLoggingInterceptor().build()

    init {
        bind<OkHttpClient>().toInstance(client)
        
        bind<GetContentManager>().toInstance(GetContentManager(client))
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