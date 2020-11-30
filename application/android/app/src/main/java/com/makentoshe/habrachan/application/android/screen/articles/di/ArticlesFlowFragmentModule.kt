package com.makentoshe.habrachan.application.android.screen.articles.di

import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.model.*
import com.makentoshe.habrachan.common.broadcast.ArticlesSearchBroadcastReceiver
import com.makentoshe.habrachan.common.database.session.SessionDao
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.di.common.ApplicationScope
import io.reactivex.disposables.CompositeDisposable
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class ArticlesFlowFragmentScope

class ArticlesFlowFragmentModule(fragment: ArticlesFlowFragment) : Module() {

    private val sessionDatabase by inject<SessionDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
        bind<SessionDao>().toInstance(sessionDatabase.session())

        val searchBroadcastReceiver = ArticlesSearchBroadcastReceiver()
        bind<ArticlesSearchBroadcastReceiver>().toInstance(searchBroadcastReceiver)
        bind<ArticlesSearchEpoxyController>().toInstance(getArticlesSearchEpoxyController())

        bind<CompositeDisposable>().toInstance(CompositeDisposable())
    }

    private fun getArticlesSearchEpoxyController() =
        ArticlesSearchEpoxyController(
            sessionDatabase.session(),
            ArticlesSearchTopEpoxyModel.Factory(),
            ArticlesSearchAllEpoxyModel.Factory(),
            ArticlesSearchInterestingEpoxyModel.Factory(),
            ArticlesSearchSubscriptionEpoxyModel.Factory(),
            ArticlesSearchCustomEpoxyModel.Factory()
        )
}