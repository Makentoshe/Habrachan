package com.makentoshe.habrachan.application.android.di

import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.network.*
import com.makentoshe.habrachan.network.articles.get.GetArticlesManager
import com.makentoshe.habrachan.network.articles.get.mobile.GetArticlesManagerImpl
import com.makentoshe.habrachan.network.manager.*
import com.makentoshe.habrachan.network.request.*
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module
import toothpick.ktp.binding.bind
import javax.net.ssl.HostnameVerifier

class NetworkModule : Module() {

    companion object : Analytics(LogAnalytic())

    private val client = OkHttpClient.Builder().followRedirects(true).addLoggingInterceptor().build()

    private val ktorHttpClient = HttpClient(OkHttp) {
        engine { preconfigured = client }
    }

    init {
        bind<OkHttpClient>().toInstance(client)

        bind<GetContentManager>().toInstance(GetContentManager(client))

        val getArticleManager = NativeGetArticleManager.Builder(client).build()
        bind<GetArticleManager<out GetArticleRequest2>>().toInstance(getArticleManager)

        bind<GetArticlesManager>().toInstance(GetArticlesManagerImpl(ktorHttpClient))

        val nativeMeManager = NativeGetMeManager.Builder(client).build()
        val loginManager = NativeLoginManager.Builder(client, nativeMeManager).build()
        bind<NativeLoginManager>().toInstance(loginManager)

        val nativeGetUserManager = NativeGetUserManager.Builder(client).build()
        bind<GetUserManager<out GetUserRequest>>().toInstance(nativeGetUserManager)

        val nativeGetCommentsManager = NativeGetArticleCommentsManager.Builder(client).build()
        bind<GetArticleCommentsManager<out GetArticleCommentsRequest>>().toInstance(nativeGetCommentsManager)

        val nativeVoteArticleManager = NativeVoteArticleManager.Builder(client).build()
        bind<VoteArticleManager<out VoteArticleRequest>>().toInstance(nativeVoteArticleManager)

        val nativeVoteCommentManager = NativeVoteCommentManager.Builder(client).build()
        bind<VoteCommentManager<out VoteCommentRequest2>>().toInstance(nativeVoteCommentManager)

        val nativePostCommentManager = NativePostCommentManager.Builder(client).build()
        bind<PostCommentManager<out PostCommentRequest>>().toInstance(nativePostCommentManager)
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