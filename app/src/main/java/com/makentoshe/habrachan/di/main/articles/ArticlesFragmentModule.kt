package com.makentoshe.habrachan.di.main.articles

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.common.database.ImageDatabase
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.articles.ArticlesFragment
import com.makentoshe.habrachan.viewmodel.main.articles.ArticlesViewModel
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class ArticlesFragmentModule(fragment: ArticlesFragment) : Module() {

    private val articleManager: HabrArticleManager

    private val client by inject<OkHttpClient>()
    private val database by inject<HabrDatabase>()
    private val router by inject<Router>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
        articleManager = HabrArticleManager.Builder(client).build("text_html")

        val articlesRequestFactory = GetArticlesRequest.Factory(database.session().get()!!)
        bind<GetArticlesRequest.Factory>().toInstance(articlesRequestFactory)

        val articlesViewModel2 = getArticlesViewModel(fragment)
        bind<ArticlesViewModel>().toInstance(articlesViewModel2)
    }

    private fun getArticlesViewModel(fragment: ArticlesFragment): ArticlesViewModel {
        val imageDatabase = ImageDatabase(fragment.requireContext())
        val factory = ArticlesViewModel.Factory(articleManager, database, imageDatabase, router)
        return ViewModelProviders.of(fragment, factory)[ArticlesViewModel::class.java]
    }
}
