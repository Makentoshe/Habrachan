package com.makentoshe.habrachan.application.android.screen.articles.model

import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModel
import com.makentoshe.habrachan.common.database.session.SessionDao
import com.makentoshe.habrachan.common.entity.session.ArticlesRequestSpec

class ArticlesSearchEpoxyController(
    private val sessionDao: SessionDao,
    private val topModelFactory: ArticlesSearchTopEpoxyModel.Factory,
    private val allModelFactory: ArticlesSearchAllEpoxyModel.Factory,
    private val interestingModelFactory: ArticlesSearchInterestingEpoxyModel.Factory,
    private val subscriptionModelFactory: ArticlesSearchSubscriptionEpoxyModel.Factory,
    private val customModelFactory: ArticlesSearchCustomEpoxyModel.Factory
) : EpoxyController() {

    private val models = listOf(
        topModelFactory.build(),
        interestingModelFactory.build(),
        allModelFactory.build(),
        subscriptionModelFactory.build()
    )

    override fun buildModels() {
        models.filter(::filterIsLoggedIn).filterNot(::filterIsDisplayed).forEach { it.addTo(this) }
        customModelFactory.build().addTo(this)
    }

    private fun filterIsLoggedIn(model: EpoxyModel<*>) : Boolean {
        if (model !is ArticlesSearchSubscriptionEpoxyModel) return true
        return sessionDao.get().isLoggedIn
    }

    private fun filterIsDisplayed(model: EpoxyModel<*>): Boolean {
        val spec = sessionDao.get().articlesRequestSpec
        if (model is ArticlesSearchTopEpoxyModel && spec is ArticlesRequestSpec.Top) {
            return true
        }
        if (model is ArticlesSearchInterestingEpoxyModel && spec is ArticlesRequestSpec.Interesting) {
            return true
        }
        if (model is ArticlesSearchAllEpoxyModel && spec is ArticlesRequestSpec.All) {
            return true
        }
        if (model is ArticlesSearchSubscriptionEpoxyModel && spec is ArticlesRequestSpec.Subscription) {
            return false
        }
        return false
    }
}
