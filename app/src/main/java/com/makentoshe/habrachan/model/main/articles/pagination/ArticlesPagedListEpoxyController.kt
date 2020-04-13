package com.makentoshe.habrachan.model.main.articles.pagination

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.model.main.articles.ArticleEpoxyModel
import com.makentoshe.habrachan.model.main.articles.ArticlesPageDivideEpoxyModel
import java.util.*

class ArticlesPagedListEpoxyController(
    private val articleModelFactory: ArticleEpoxyModel.Factory,
    private val divideModelFactory: ArticlesPageDivideEpoxyModel.Factory
) : PagedListEpoxyController<Article>() {

    val pageSize = 20

    override fun buildItemModel(currentPosition: Int, item: Article?): EpoxyModel<*> {
        return articleModelFactory.build(currentPosition, item!!)
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        var page = 2
        models.chunked(20).flatMap {
            LinkedList(it).apply { addLast(divideModelFactory.build(-page, page++)) }
        }.also { super.addModels(it) }
    }
}

