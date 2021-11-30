package com.makentoshe.habrachan.application.android.di

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.common.di.module.BaseNetworkModule
import com.makentoshe.habrachan.network.*
import com.makentoshe.habrachan.network.articles.get.GetArticlesManager
import com.makentoshe.habrachan.network.articles.get.mobile.GetArticlesManagerImpl
import com.makentoshe.habrachan.network.login.GetCookieManager
import com.makentoshe.habrachan.network.login.GetLoginManager
import com.makentoshe.habrachan.network.manager.*
import com.makentoshe.habrachan.network.request.*
import okhttp3.OkHttpClient
import toothpick.ktp.binding.bind

class NetworkModule : BaseNetworkModule() {

    companion object : Analytics(LogAnalytic())

    init {
        bind<OkHttpClient>().toInstance(okHttpClient)

        bind<GetContentManager>().toInstance(GetContentManager(okHttpClient))

        val getArticleManager = NativeGetArticleManager.Builder(okHttpClient).build()
        bind<GetArticleManager<out GetArticleRequest2>>().toInstance(getArticleManager)

        bind<GetArticlesManager>().toInstance(GetArticlesManagerImpl(ktorHttpClient))

        val nativeMeManager = NativeGetMeManager.Builder(okHttpClient).build()
        val loginManager = NativeLoginManager.Builder(okHttpClient, nativeMeManager).build()
        bind<NativeLoginManager>().toInstance(loginManager)

        val nativeGetUserManager = NativeGetUserManager.Builder(okHttpClient).build()
        bind<GetUserManager<out GetUserRequest>>().toInstance(nativeGetUserManager)

        val nativeGetCommentsManager = NativeGetArticleCommentsManager.Builder(okHttpClient).build()
        bind<GetArticleCommentsManager<out GetArticleCommentsRequest>>().toInstance(nativeGetCommentsManager)

        val nativeVoteArticleManager = NativeVoteArticleManager.Builder(okHttpClient).build()
        bind<VoteArticleManager<out VoteArticleRequest>>().toInstance(nativeVoteArticleManager)

        val nativeVoteCommentManager = NativeVoteCommentManager.Builder(okHttpClient).build()
        bind<VoteCommentManager<out VoteCommentRequest2>>().toInstance(nativeVoteCommentManager)

        val nativePostCommentManager = NativePostCommentManager.Builder(okHttpClient).build()
        bind<PostCommentManager<out PostCommentRequest>>().toInstance(nativePostCommentManager)

        val getCookieManager = GetCookieManager(ktorHttpClient)
        bind<GetCookieManager>().toInstance(getCookieManager)

        val getLoginManager = GetLoginManager(ktorHttpClient)
        bind<GetLoginManager>().toInstance(getLoginManager)
    }

}