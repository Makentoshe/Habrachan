package com.makentoshe.habrachan.model.main.articles

import com.airbnb.epoxy.EpoxyController
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest

class ArticlesSearchEpoxyController(
    private val sessionDao: SessionDao,
    private val topModelFactory: ArticlesSearchTopEpoxyModel.Factory,
    private val allModelFactory: ArticlesSearchAllEpoxyModel.Factory,
    private val interestingModelFactory: ArticlesSearchInterestingEpoxyModel.Factory,
    private val subscriptionModelFactory: ArticlesSearchSubscriptionEpoxyModel.Factory,
    private val customModelFactory: ArticlesSearchCustomEpoxyModel.Factory
) : EpoxyController() {

    override fun buildModels() {
        listOf(
            topModelFactory.build(),
            interestingModelFactory.build(),
            allModelFactory.build(),
            subscriptionModelFactory.build(),
            customModelFactory.build()
        ).filter(::filter).forEach {
            it.addTo(this)
        }
    }

    private fun filter(model: ArticlesSearchEpoxyModel<*>): Boolean {
        val userSession = sessionDao.get()!!
        val currentFactor = model.requestSpec != userSession.articlesRequestSpec
        val isSubscriptionModel = model.requestSpec == GetArticlesRequest.Spec.subscription()
        val loginInFactor = if (isSubscriptionModel) userSession.isLoggedIn else true
        return currentFactor && loginInFactor
    }
}
