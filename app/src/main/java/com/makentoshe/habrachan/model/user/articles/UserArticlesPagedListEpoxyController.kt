package com.makentoshe.habrachan.model.user.articles

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.model.main.articles.model.ArticleEpoxyModel

class UserArticlesPagedListEpoxyController(
    private val articleModelFactory: ArticleEpoxyModel.Factory
) : PagedListEpoxyController<Article>() {

    val pageSize = 20

    override fun buildItemModel(currentPosition: Int, item: Article?): EpoxyModel<*> {
        return articleModelFactory.build(currentPosition, enableSmartDivide = false, post = item!!)
    }
}

