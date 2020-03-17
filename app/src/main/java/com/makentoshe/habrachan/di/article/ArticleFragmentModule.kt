package com.makentoshe.habrachan.di.article

import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.article.JavaScriptInterface
import com.makentoshe.habrachan.model.article.WebViewController
import com.makentoshe.habrachan.view.article.ArticleFragment
import com.makentoshe.habrachan.viewmodel.article.ArticleFragmentViewModel
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.article.VoteArticleViewModel
import com.makentoshe.habrachan.common.navigation.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class ArticleFragmentModule(fragment: ArticleFragment) : Module() {

    private val javascriptInterface = JavaScriptInterface()

    private val router by inject<Router>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

        bind<WebViewController>().toInstance(WebViewController(fragment, javascriptInterface))
        bind<ArticleFragment.Navigator>().toInstance(ArticleFragment.Navigator(router))
        bind<JavaScriptInterface>().toInstance(javascriptInterface)

        val userAvatarViewModelProvider = UserAvatarViewModelProvider(fragment)
        bind<UserAvatarViewModel>().toProviderInstance(userAvatarViewModelProvider)
        val articleFragmentViewModelProvider = ArticleFragmentViewModelProvider(fragment, userAvatarViewModelProvider)
        bind<ArticleFragmentViewModel>().toProviderInstance(articleFragmentViewModelProvider)

        bind<VoteArticleViewModel>().toProviderInstance(VoteArticleViewModelProvider(fragment))
    }
}