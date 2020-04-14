package com.makentoshe.habrachan.model.main.articles

import com.airbnb.epoxy.EpoxyModel
import com.makentoshe.habrachan.common.entity.session.UserSession

abstract class ArticlesSearchEpoxyModel<V>: EpoxyModel<V>() {
    abstract val requestSpec: UserSession.ArticlesRequestSpec
}