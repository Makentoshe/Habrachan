package com.makentoshe.habrachan.di.article

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.common.database.ImageDatabase
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.manager.ArticlesManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.article.JavaScriptInterface
import com.makentoshe.habrachan.model.article.WebViewController
import com.makentoshe.habrachan.view.article.ArticleFragment
import com.makentoshe.habrachan.viewmodel.article.ArticleFragmentViewModel
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.article.VoteArticleViewModel
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class ArticleFragmentModule(fragment: ArticleFragment) : Module() {

    private val imageDatabase = ImageDatabase(fragment.requireContext())
    private val javascriptInterface = JavaScriptInterface()
    private val avatarManager : ImageManager
    private val articlesManager : ArticlesManager

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val database by inject<HabrDatabase>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        avatarManager = ImageManager.Builder(client).build()
        articlesManager = ArticlesManager.Builder(client).build()

        bind<WebViewController>().toInstance(WebViewController(fragment, javascriptInterface))
        bind<ArticleFragment.Navigator>().toInstance(ArticleFragment.Navigator(router, database.session()))
        bind<JavaScriptInterface>().toInstance(javascriptInterface)

        val userAvatarViewModel = getUserAvatarViewModel(fragment)
        bind<UserAvatarViewModel>().toInstance(userAvatarViewModel)

        val articleFragmentViewModelProvider = getArticleFragmentViewModel(fragment)
        bind<ArticleFragmentViewModel>().toInstance(articleFragmentViewModelProvider)

        val voteArticleViewModel = getVoteArticleViewModel(fragment)
        bind<VoteArticleViewModel>().toInstance(voteArticleViewModel)
    }

    private fun getUserAvatarViewModel(fragment: ArticleFragment): UserAvatarViewModel {
        val application = fragment.requireActivity().application
        val factory = UserAvatarViewModel.Factory(imageDatabase.avatars(), application, avatarManager)
        return ViewModelProviders.of(fragment, factory)[UserAvatarViewModel::class.java]
    }

    private fun getArticleFragmentViewModel(fragment: ArticleFragment) : ArticleFragmentViewModel {
        val userAvatarViewModel = getUserAvatarViewModel(fragment)
        val factory = ArticleFragmentViewModel.Factory(
            articlesManager, database.articles(), database.session(), userAvatarViewModel
        )
        return ViewModelProviders.of(fragment, factory)[ArticleFragmentViewModel::class.java]
    }

    private fun getVoteArticleViewModel(fragment: ArticleFragment): VoteArticleViewModel {
        val factory = VoteArticleViewModel.Factory(database.session(), articlesManager)
        return ViewModelProviders.of(fragment, factory)[VoteArticleViewModel::class.java]
    }
}