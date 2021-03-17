package com.makentoshe.habrachan.application.android.di

import android.content.Context
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.network.deserializer.NativeGetArticleDeserializer
import com.makentoshe.habrachan.network.deserializer.NativeGetArticlesDeserializer
import com.makentoshe.habrachan.network.manager.*
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.request.GetArticlesSpec
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

        val getArticleManager = NativeGetArticleManager.Builder(client, NativeGetArticleDeserializer()).build()
        bind<GetArticleManager<out GetArticleRequest2>>().toInstance(getArticleManager)

        val getArticlesManager = NativeGetArticlesManager.Builder(client, NativeGetArticlesDeserializer()).build()
        bind<GetArticlesManager<out GetArticlesRequest2, out GetArticlesSpec>>().toInstance(getArticlesManager)
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