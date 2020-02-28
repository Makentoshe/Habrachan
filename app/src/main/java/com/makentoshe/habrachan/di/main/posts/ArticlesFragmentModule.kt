package com.makentoshe.habrachan.di.main.posts

import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.database.CommentDao
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.posts.ArticlesFragment
import com.makentoshe.habrachan.viewmodel.main.posts.ArticlesViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class ArticlesFragmentModule(fragment: ArticlesFragment) : Module() {

    private val manager by inject<HabrArticleManager>()
    private val factory by inject<GetArticlesRequest.Builder>()
    private val articleDao by inject<ArticleDao>()
    private val commentDao by inject<CommentDao>()
    private val avatarDao by inject<AvatarDao>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

        bind<ArticlesViewModel>().toProviderInstance(ArticlesViewModelProvider(fragment))
    }
}
