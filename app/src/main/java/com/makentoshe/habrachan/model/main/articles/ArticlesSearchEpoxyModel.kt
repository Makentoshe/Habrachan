package com.makentoshe.habrachan.model.main.articles

import com.airbnb.epoxy.EpoxyModel
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest

abstract class ArticlesSearchEpoxyModel<V> : EpoxyModel<V>() {
    abstract val requestSpec: GetArticlesRequest.Spec
}