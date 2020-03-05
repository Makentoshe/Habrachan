package com.makentoshe.habrachan.di.article

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.article.ArticleFragment
import com.makentoshe.habrachan.viewmodel.article.VoteArticleViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class VoteArticleViewModelProvider(private val fragment: ArticleFragment) : Provider<VoteArticleViewModel> {

    private val articleManager by inject<HabrArticleManager>()
    private val sessionDao by inject<SessionDao>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }

    override fun get(): VoteArticleViewModel {
        val factory = VoteArticleViewModel.Factory(sessionDao, articleManager)
        return ViewModelProviders.of(fragment, factory)[VoteArticleViewModel::class.java]
    }
}