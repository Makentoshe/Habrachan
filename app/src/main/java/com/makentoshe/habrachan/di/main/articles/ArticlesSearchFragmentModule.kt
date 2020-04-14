package com.makentoshe.habrachan.di.main.articles

import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.main.articles.*
import com.makentoshe.habrachan.view.main.articles.ArticlesSearchFragment
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class ArticlesSearchFragmentModule(fragment: ArticlesSearchFragment): Module() {

    private val cacheDatabase by inject<HabrDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
        val controller = getArticlesSearchEpoxyController()
        bind<ArticlesSearchEpoxyController>().toInstance(controller)
    }

    private fun getArticlesSearchEpoxyController() = ArticlesSearchEpoxyController(
        cacheDatabase.session(),
        ArticlesSearchTopEpoxyModel.Factory(),
        ArticlesSearchAllEpoxyModel.Factory(),
        ArticlesSearchInterestingEpoxyModel.Factory(),
        ArticlesSearchSubscriptionEpoxyModel.Factory()
    )

}
