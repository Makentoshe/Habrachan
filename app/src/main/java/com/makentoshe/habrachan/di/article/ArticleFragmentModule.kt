package com.makentoshe.habrachan.di.article

import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.common.database.ImageDatabase
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.article.JavaScriptInterface
import com.makentoshe.habrachan.model.article.WebViewController
import com.makentoshe.habrachan.view.article.ArticleFragment
import com.makentoshe.habrachan.viewmodel.article.ArticleFragmentViewModel
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.article.VoteArticleViewModel
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class ArticleFragmentModule(fragment: ArticleFragment) : Module() {

    private val imageDatabase = ImageDatabase(fragment.requireContext())
    private val javascriptInterface = JavaScriptInterface()
    private val avatarManager : ImageManager
    private val articleManager : HabrArticleManager

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val database by inject<HabrDatabase>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        avatarManager = ImageManager.Builder(client).build()
        articleManager = HabrArticleManager.Builder(client).build("text_html")

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
            articleManager, database.articles(), database.session(), userAvatarViewModel
        )
        return ViewModelProviders.of(fragment, factory)[ArticleFragmentViewModel::class.java]
    }

    private fun getVoteArticleViewModel(fragment: ArticleFragment): VoteArticleViewModel {
        val factory = VoteArticleViewModel.Factory(database.session(), articleManager)
        return ViewModelProviders.of(fragment, factory)[VoteArticleViewModel::class.java]
    }
}