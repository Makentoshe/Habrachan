package com.makentoshe.habrachan.di.main.posts

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.database.CommentDao
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.posts.ArticlesFragment
import com.makentoshe.habrachan.viewmodel.main.posts.ArticlesViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class ArticlesViewModelProvider(private val fragment: ArticlesFragment) : Provider<ArticlesViewModel> {

    private val manager by inject<HabrArticleManager>()
    private val sessionDao by inject<SessionDao>()
    private val articleDao by inject<ArticleDao>()
    private val commentDao by inject<CommentDao>()
    private val avatarDao by inject<AvatarDao>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }

    override fun get(): ArticlesViewModel {
        val factory = ArticlesViewModel.Factory(sessionDao, manager, articleDao, commentDao, avatarDao)
        return ViewModelProviders.of(fragment, factory)[ArticlesViewModel::class.java]
    }

}