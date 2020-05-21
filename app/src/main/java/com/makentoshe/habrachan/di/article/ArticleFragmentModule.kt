package com.makentoshe.habrachan.di.article

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.navigation.Router
import com.makentoshe.habrachan.common.network.manager.ArticlesManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.article.ArticleFragment
import com.makentoshe.habrachan.viewmodel.article.ArticleFragmentViewModel
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.article.VoteArticleViewModel
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class ArticleFragmentModule(fragment: ArticleFragment) : Module() {

    private val avatarManager : ImageManager
    private val articlesManager : ArticlesManager

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val database by inject<CacheDatabase>()
    private val sessionDatabase by inject<SessionDatabase>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        avatarManager = ImageManager.Builder(client).build()
        articlesManager = ArticlesManager.Builder(client).build()

        bind<CompositeDisposable>().toInstance(CompositeDisposable())

        bind<ArticleFragment.Navigator>().toInstance(ArticleFragment.Navigator(router, sessionDatabase.session()))

        val userAvatarViewModel = getUserAvatarViewModel(fragment)
        bind<UserAvatarViewModel>().toInstance(userAvatarViewModel)

        val articleFragmentViewModelProvider = getArticleFragmentViewModel(fragment)
        bind<ArticleFragmentViewModel>().toInstance(articleFragmentViewModelProvider)

        val voteArticleViewModel = getVoteArticleViewModel(fragment)
        bind<VoteArticleViewModel>().toInstance(voteArticleViewModel)
    }

    private fun getUserAvatarViewModel(fragment: ArticleFragment): UserAvatarViewModel {
        val application = fragment.requireActivity().application
        val factory = UserAvatarViewModel.Factory(database.avatars(), application, avatarManager)
        return ViewModelProviders.of(fragment, factory)[UserAvatarViewModel::class.java]
    }

    private fun getArticleFragmentViewModel(fragment: ArticleFragment) : ArticleFragmentViewModel {
        val userAvatarViewModel = getUserAvatarViewModel(fragment)
        val factory = ArticleFragmentViewModel.Factory(
            articlesManager, database.articles(), sessionDatabase.session(), userAvatarViewModel
        )
        return ViewModelProviders.of(fragment, factory)[ArticleFragmentViewModel::class.java]
    }

    private fun getVoteArticleViewModel(fragment: ArticleFragment): VoteArticleViewModel {
        val factory = VoteArticleViewModel.Factory(sessionDatabase.session(), articlesManager)
        return ViewModelProviders.of(fragment, factory)[VoteArticleViewModel::class.java]
    }
}