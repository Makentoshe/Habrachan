package com.makentoshe.habrachan.model.main.articles

import android.view.ViewGroup
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest

class ArticlesSearchCustomEpoxyModel(): ArticlesSearchEpoxyModel<ViewGroup>() {
    override val requestSpec = GetArticlesRequest.Spec.search("relevance")

    override fun bind(view: ViewGroup) {

    }

    override fun getDefaultLayout(): Int {
        return R.layout.articles_search_element_custom
    }

    class Factory {
        fun build(): ArticlesSearchCustomEpoxyModel {
            val model = ArticlesSearchCustomEpoxyModel()
            model.id("custom")
            return model
        }
    }
}