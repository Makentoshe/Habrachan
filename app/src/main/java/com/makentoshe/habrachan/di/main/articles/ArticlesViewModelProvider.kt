package com.makentoshe.habrachan.di.main.articles

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.*
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.articles.ArticlesFragment
import com.makentoshe.habrachan.viewmodel.main.articles.ArticlesViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class ArticlesViewModelProvider(private val fragment: ArticlesFragment) : Provider<ArticlesViewModel> {

    private val manager by inject<HabrArticleManager>()
    private val sessionDao by inject<SessionDao>()
    private val articleDao by inject<ArticleDao>()
    private val commentDao by inject<CommentDao>()
    private val avatarDao by inject<AvatarDao>()
    private val userDao by inject<UserDao>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }

    override fun get(): ArticlesViewModel {
        val factory = ArticlesViewModel.Factory(sessionDao, manager, articleDao, commentDao, avatarDao, userDao)
        return ViewModelProviders.of(fragment, factory)[ArticlesViewModel::class.java]
    }

}