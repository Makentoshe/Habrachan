package com.makentoshe.habrachan.application.android.common.articles.viewmodel

import com.makentoshe.habrachan.network.request.SpecType

data class GetArticlesSpec(
    val page: Int, val specType: SpecType, val pageSize: Int = 20
)