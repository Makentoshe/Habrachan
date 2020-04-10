package com.makentoshe.habrachan.di.main.articles

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.main.articles.*
import com.makentoshe.habrachan.viewmodel.main.articles.ArticlesControllerViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class ArticlesControllerViewModelProvider(private val fragment: Fragment) : Provider<ArticlesControllerViewModel> {

    private val router by inject<Router>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }

    override fun get(): ArticlesControllerViewModel {
        val controller = getArticlesEpoxyController()
        val factory = ArticlesControllerViewModel.Factory(controller)
        return ViewModelProviders.of(fragment, factory)[ArticlesControllerViewModel::class.java]
    }

    private fun getArticlesEpoxyController() = ArticlesEpoxyController(
        ArticleEpoxyModel.Factory(router),
        ArticleDivideEpoxyModel.Factory(),
        ArticlesPageDivideEpoxyModel.Factory(),
        ArticlesSearchModel.Factory()
    )
}