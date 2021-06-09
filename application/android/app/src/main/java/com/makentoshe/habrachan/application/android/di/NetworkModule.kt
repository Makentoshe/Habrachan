package com.makentoshe.habrachan.application.android.di

import android.content.Context
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.network.deserializer.*
import com.makentoshe.habrachan.network.manager.*
import com.makentoshe.habrachan.network.request.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module
import toothpick.ktp.binding.bind
import javax.net.ssl.HostnameVerifier

class NetworkModule(context: Context) : Module() {

    private val client = OkHttpClient.Builder().followRedirects(true).addLoggingInterceptor().build()

    init {
        bind<OkHttpClient>().toInstance(client)

        bind<GetContentManager>().toInstance(GetContentManager(client))

        val getArticleManager = NativeGetArticleManager.Builder(client, NativeGetArticleDeserializer()).build()
        bind<GetArticleManager<out GetArticleRequest2>>().toInstance(getArticleManager)

        val getArticlesManager = NativeGetArticlesManager.Builder(client, NativeGetArticlesDeserializer()).build()
        bind<GetArticlesManager<out GetArticlesRequest2, out GetArticlesSpec>>().toInstance(getArticlesManager)

        val nativeMeManager = NativeGetMeManager.Builder(client, NativeGetMeDeserializer()).build()
        val loginManager = NativeLoginManager.Builder(client, NativeLoginDeserializer(), nativeMeManager).build()
        bind<NativeLoginManager>().toInstance(loginManager)

        val nativeGetUserManager = NativeGetUserManager.Builder(client, NativeGetUserDeserializer()).build()
        bind<GetUserManager<out GetUserRequest>>().toInstance(nativeGetUserManager)

        val nativeGetCommentsManager = NativeGetArticleCommentsManager.Builder(client, NativeGetCommentsDeserializer()).build()
        bind<GetArticleCommentsManager<out GetArticleCommentsRequest>>().toInstance(nativeGetCommentsManager)

        val nativeVoteArticleManager = NativeVoteArticleManager.Builder(client).build()
        bind<VoteArticleManager<out VoteArticleRequest>>().toInstance(nativeVoteArticleManager)
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