package com.makentoshe.habrachan.application.android.di

import android.content.Context
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.network.NativeLoginManager
import com.makentoshe.habrachan.network.NativeVoteArticleManager
import com.makentoshe.habrachan.network.NativeVoteCommentManager
import com.makentoshe.habrachan.network.deserializer.*
import com.makentoshe.habrachan.network.manager.*
import com.makentoshe.habrachan.network.request.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module
import toothpick.ktp.binding.bind
import javax.net.ssl.HostnameVerifier

class NetworkModule(context: Context) : Module() {

    companion object: Analytics(LogAnalytic())

    private val client = OkHttpClient.Builder().followRedirects(true).addLoggingInterceptor().build()

    init {
        bind<OkHttpClient>().toInstance(client)

        bind<GetContentManager>().toInstance(GetContentManager(client))

        val getArticleManager = NativeGetArticleManager.Builder(client, NativeGetArticleDeserializer()).build()
        bind<GetArticleManager<out GetArticleRequest2>>().toInstance(getArticleManager)

        val getArticlesManager = NativeGetArticlesManager.Builder(client, NativeGetArticlesDeserializer()).build()
        bind<GetArticlesManager<out GetArticlesRequest2, out GetArticlesSpec>>().toInstance(getArticlesManager)

        val nativeMeManager = NativeGetMeManager.Builder(client, NativeGetMeDeserializer()).build()
        val loginManager = NativeLoginManager.Builder(client, nativeMeManager).build()
        bind<NativeLoginManager>().toInstance(loginManager)

        val nativeGetUserManager = NativeGetUserManager.Builder(client, NativeGetUserDeserializer()).build()
        bind<GetUserManager<out GetUserRequest>>().toInstance(nativeGetUserManager)

        val nativeGetCommentsManager = NativeGetArticleCommentsManager.Builder(client, NativeGetCommentsDeserializer()).build()
        bind<GetArticleCommentsManager<out GetArticleCommentsRequest>>().toInstance(nativeGetCommentsManager)

        val nativeVoteArticleManager = NativeVoteArticleManager.Builder(client).build()
        bind<VoteArticleManager<out VoteArticleRequest>>().toInstance(nativeVoteArticleManager)

        val nativeVoteCommentManager = NativeVoteCommentManager.Builder(client).build()
        bind<VoteCommentManager<out VoteCommentRequest2>>().toInstance(nativeVoteCommentManager)
    }

    private fun OkHttpClient.Builder.addLoggingInterceptor(): OkHttpClient.Builder {
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor {
                capture(analyticEvent("OkHttpClient", it))
            }
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