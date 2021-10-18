package com.makentoshe.habrachan.application.android.common.articles.viewmodel

import com.makentoshe.habrachan.api.articles.ArticlesFilter

data class GetArticlesSpec(
    val articlesFilters: Set<ArticlesFilter>,  val pageSize: Int = 20
)