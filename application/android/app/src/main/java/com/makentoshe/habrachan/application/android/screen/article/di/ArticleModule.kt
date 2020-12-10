package com.makentoshe.habrachan.application.android.screen.article.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.arena.ArticleArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleNavigation
import com.makentoshe.habrachan.application.android.screen.article.viewmodel.ArticleViewModel
import com.makentoshe.habrachan.application.core.arena.articles.ArticleArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.ArticlesManager
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class ArticleScope

class ArticleModule(fragment: ArticleFragment) : Module() {

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val session by inject<UserSession>()

    private val androidCacheDatabase by inject<AndroidCacheDatabase>()

    init {
        Toothpick.openScope(ApplicationScope::class).inject(this)
        bind<CompositeDisposable>().toInstance(CompositeDisposable())
        bind<ArticleNavigation>().toInstance(ArticleNavigation(router))

        val articleArenaCache = ArticleArenaCache()
        val articleArena = ArticleArena(ArticlesManager.Builder(client).native(), articleArenaCache)
        val viewModelFactory = ArticleViewModel.Factory(CompositeDisposable(), session, articleArena, fragment.arguments)
        val viewModel = ViewModelProviders.of(fragment, viewModelFactory)[ArticleViewModel::class.java]
        bind<ArticleViewModel>().toInstance(viewModel)

//        avatarManager = ImageManager.Builder(client).build()
//        articlesManager = ArticlesManager.Builder(client).build()
//
//
//        bind<ArticleNavigation>().toInstance(ArticleNavigation(router, sessionDatabase.session()))
//
//        val userAvatarViewModel = getUserAvatarViewModel(fragment)
//        bind<UserAvatarViewModel>().toInstance(userAvatarViewModel)
//
//        val articleFragmentViewModelProvider = getArticleFragmentViewModel(fragment)
//        bind<ArticleViewModel>().toInstance(articleFragmentViewModelProvider)
//
//        val voteArticleViewModel = getVoteArticleViewModel(fragment)
//        bind<VoteArticleViewModel>().toInstance(voteArticleViewModel)
//
//        bind<JavaScriptInterface>().toInstance(JavaScriptInterface())
    }

//    private fun getUserAvatarViewModel(fragment: ArticleFragment): UserAvatarViewModel {
//        val application = fragment.requireActivity().application
//        val factory = UserAvatarViewModel.Factory(database.avatars(), application, avatarManager)
//        return ViewModelProviders.of(fragment, factory)[UserAvatarViewModel::class.java]
//    }
//
//    private fun getArticleFragmentViewModel(fragment: ArticleFragment): ArticleViewModel {
//        val userAvatarViewModel = getUserAvatarViewModel(fragment)
//        val factory = ArticleViewModel.Factory(
//            articlesManager, database.articles(), sessionDatabase.session(), userAvatarViewModel
//        )
//        return ViewModelProviders.of(fragment, factory)[ArticleViewModel::class.java]
//    }
//
//    private fun getVoteArticleViewModel(fragment: ArticleFragment): VoteArticleViewModel {
//        val factory = VoteArticleViewModel.Factory(sessionDatabase.session(), articlesManager)
//        return ViewModelProviders.of(fragment, factory)[VoteArticleViewModel::class.java]
//    }
}