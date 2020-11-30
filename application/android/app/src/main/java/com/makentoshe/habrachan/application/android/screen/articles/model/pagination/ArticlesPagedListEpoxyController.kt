package com.makentoshe.habrachan.application.android.screen.articles.model.pagination

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticleEpoxyModel
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesPageDivideEpoxyModel
import com.makentoshe.habrachan.common.entity.Article

class ArticlesPagedListEpoxyController(
    private val articleModelFactory: ArticleEpoxyModel.Factory,
    private val divideModelFactory: ArticlesPageDivideEpoxyModel.Factory
) : PagedListEpoxyController<Article>() {

    val pageSize = 20

    override fun buildItemModel(currentPosition: Int, item: Article?): EpoxyModel<*> {
        return articleModelFactory.build(currentPosition, enableSmartDivide = true, article = item!!)
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        var page = 2
        models.chunked(20).flatMap {
            it.toMutableList().apply { add(divideModelFactory.build(page.toString(), page++)) }
        }.let {
            super.addModels(it.dropLast(1))
        }
    }
}

