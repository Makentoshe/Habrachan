package com.makentoshe.habrachan.application.android.screen.articles.flow.model

import com.makentoshe.habrachan.network.request.SpecType

data class ArticlesSpec(val page: Int, val specType: SpecType) {
    companion object {
        const val PAGE_SIZE = 20
    }
}