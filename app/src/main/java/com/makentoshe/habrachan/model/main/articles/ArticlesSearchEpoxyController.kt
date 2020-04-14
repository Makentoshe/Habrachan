package com.makentoshe.habrachan.model.main.articles

import com.airbnb.epoxy.EpoxyController
import com.makentoshe.habrachan.common.entity.session.UserSession

class ArticlesSearchEpoxyController(
    private val userSession: UserSession,
    private val topModelFactory : ArticlesSearchTopEpoxyModel.Factory,
    private val allModelFactory: ArticlesSearchAllEpoxyModel.Factory,
    private val interestingModelFactory: ArticlesSearchInterestingEpoxyModel.Factory,
    private val subscriptionModelFactory: ArticlesSearchSubscriptionEpoxyModel.Factory
) : EpoxyController() {

    override fun buildModels() {
        listOf(
            topModelFactory.build(),
            interestingModelFactory.build(),
            allModelFactory.build(),
            subscriptionModelFactory.build()
        ).filterNot {
            it.requestSpec == userSession.articlesRequestSpec
        }.forEach {
            it.addTo(this)
        }
    }
}
