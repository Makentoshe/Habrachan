package com.makentoshe.habrachan.di.article

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.viewmodel.article.ArticleFragmentViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class ArticleFragmentViewModelProvider(private val fragment: Fragment) : Provider<ArticleFragmentViewModel> {

    private val articleDao by inject<ArticleDao>()
    private val manager by inject<HabrArticleManager>()
    private val sessionDao by inject<SessionDao>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }

    override fun get(): ArticleFragmentViewModel {
        val factory = ArticleFragmentViewModel.Factory(manager, articleDao, sessionDao)
        return ViewModelProviders.of(fragment, factory)[ArticleFragmentViewModel::class.java]
    }
}
