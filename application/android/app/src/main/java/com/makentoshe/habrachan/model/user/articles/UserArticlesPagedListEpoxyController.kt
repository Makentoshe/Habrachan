package com.makentoshe.habrachan.model.user.articles

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticleEpoxyModel
import com.makentoshe.habrachan.entity.Article

class UserArticlesPagedListEpoxyController(
    private val articleModelFactory: ArticleEpoxyModel.Factory
) : PagedListEpoxyController<Article>() {

    override fun buildItemModel(currentPosition: Int, item: Article?): EpoxyModel<*> {
        return articleModelFactory.build(currentPosition, enableSmartDivide = false, article = item!!)
    }
}

