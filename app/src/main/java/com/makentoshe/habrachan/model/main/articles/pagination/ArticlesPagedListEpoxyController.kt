package com.makentoshe.habrachan.model.main.articles.pagination

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.model.main.articles.ArticleEpoxyModel
import com.makentoshe.habrachan.model.main.articles.ArticlesPageDivideEpoxyModel
import com.makentoshe.habrachan.model.main.articles.ArticlesPageRequestEpoxyModel

class ArticlesPagedListEpoxyController(
    private val articleModelFactory: ArticleEpoxyModel.Factory,
    private val divideModelFactory: ArticlesPageDivideEpoxyModel.Factory,
    private val requestModelFactory: ArticlesPageRequestEpoxyModel.Factory
) : PagedListEpoxyController<Article>() {

    val pageSize = 20

    override fun buildItemModel(currentPosition: Int, item: Article?): EpoxyModel<*> {
        return articleModelFactory.build(currentPosition, item!!)
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        var page = 2
        models.chunked(20).flatMap {
            it.toMutableList().apply { add(divideModelFactory.build(page.toString(), page++)) }
        }.let {
            it.toMutableList().apply { add(requestModelFactory.build()) }
        }.let {
            super.addModels(it)
        }
    }
}

