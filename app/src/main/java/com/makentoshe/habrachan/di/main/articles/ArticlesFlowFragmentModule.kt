package com.makentoshe.habrachan.di.main.articles

import com.makentoshe.habrachan.common.database.session.SessionDao
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.main.articles.*
import com.makentoshe.habrachan.view.main.articles.ArticlesFlowFragment
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class ArticlesFlowFragmentModule(fragment: ArticlesFlowFragment) : Module() {

    private val sessionDatabase by inject<SessionDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
        bind<SessionDao>().toInstance(sessionDatabase.session())

        val searchBroadcastReceiver = ArticlesSearchBroadcastReceiver()
        bind<ArticlesSearchBroadcastReceiver>().toInstance(searchBroadcastReceiver)
        bind<ArticlesSearchEpoxyController>().toInstance(getArticlesSearchEpoxyController())
    }

    private fun getArticlesSearchEpoxyController() = ArticlesSearchEpoxyController(
        sessionDatabase.session(),
        ArticlesSearchTopEpoxyModel.Factory(),
        ArticlesSearchAllEpoxyModel.Factory(),
        ArticlesSearchInterestingEpoxyModel.Factory(),
        ArticlesSearchSubscriptionEpoxyModel.Factory(),
        ArticlesSearchCustomEpoxyModel.Factory()
    )
}