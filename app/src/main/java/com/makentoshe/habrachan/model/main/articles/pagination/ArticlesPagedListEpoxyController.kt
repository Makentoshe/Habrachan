package com.makentoshe.habrachan.model.main.articles.pagination

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.model.main.articles.ArticleEpoxyModel

class ArticlesPagedListEpoxyController(
    private val articleModelFactory: ArticleEpoxyModel.Factory
) : PagedListEpoxyController<Article>() {

    override fun buildItemModel(currentPosition: Int, item: Article?): EpoxyModel<*> {
        return articleModelFactory.build(currentPosition, item!!)
    }
}

