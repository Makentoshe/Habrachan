package com.makentoshe.habrachan.application.android.screen.article.di.module

import com.makentoshe.habrachan.application.android.common.di.module.BaseNetworkModule
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.common.article.voting.VoteArticleArena
import com.makentoshe.habrachan.network.manager.VoteArticleManager
import com.makentoshe.habrachan.network.request.VoteArticleRequest
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class VoteArticleNetworkModule : BaseNetworkModule() {

    private val voteArticleManager by inject<VoteArticleManager<VoteArticleRequest>>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        bind<VoteArticleArena>().toInstance(VoteArticleArena.Factory(voteArticleManager).default())
    }

}