package com.makentoshe.habrachan.application.android.screen.articles.model

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.makentoshe.habrachan.entity.Article

class ArticlesPagedListEpoxyController(
    private val articleModelFactory: ArticleEpoxyModel.Factory,
    private val divideModelFactory: DivideEpoxyModel.Factory,
    private val footerEpoxyModel: FooterEpoxyModel.Factory
) : PagedListEpoxyController<Article>() {

    val pageSize = 20

    private val footerModel = footerEpoxyModel.build()

    override fun buildItemModel(currentPosition: Int, item: Article?): EpoxyModel<*> =
        articleModelFactory.build(currentPosition, enableSmartDivide = true, article = item!!)

    override fun addModels(models: List<EpoxyModel<*>>) {
        var page = 2
        val models = models.chunked(20).flatMap {
            it.toMutableList().apply { add(divideModelFactory.build(page.toString(), page++)) }
        }

        val lastIndex = models.indexOf(footerModel)
        if (lastIndex != -1) {
            moveModel(lastIndex, models.lastIndex)
            super.addModels(models)
        } else {
            super.addModels(models.plus(footerModel))
        }
    }
}

