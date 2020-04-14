package com.makentoshe.habrachan.di.main.articles

import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.main.articles.ArticlesSearchBroadcastReceiver
import com.makentoshe.habrachan.view.main.articles.ArticlesFlowFragment
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class ArticlesFlowFragmentModule(fragment: ArticlesFlowFragment): Module() {

    private val database by inject<HabrDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
        bind<SessionDao>().toInstance(database.session())

        val searchBroadcastReceiver = ArticlesSearchBroadcastReceiver()
        bind<ArticlesSearchBroadcastReceiver>().toInstance(searchBroadcastReceiver)
    }
}