package com.makentoshe.habrachan.application.android.screen.comments.di.module

import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.GetArticleCommentsManager
import com.makentoshe.habrachan.network.manager.GetContentManager
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.delegate.inject

class ArticleCommentsModule(
    private val fragment: ArticleCommentsFragment,
) : Module() {

    // From ApplicationScope
    private val session by inject<UserSession>()
    private val database by inject<AndroidCacheDatabase>()

    private val getContentManager by inject<GetContentManager>()
    private val getCommentsManager by inject<GetArticleCommentsManager<GetArticleCommentsRequest>>()

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class).inject(this)

    }
}
