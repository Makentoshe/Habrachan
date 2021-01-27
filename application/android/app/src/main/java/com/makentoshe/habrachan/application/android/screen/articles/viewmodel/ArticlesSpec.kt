package com.makentoshe.habrachan.application.android.screen.articles.viewmodel

import com.makentoshe.habrachan.network.request.GetArticlesRequest

data class ArticlesSpec(val page: Int, val spec: GetArticlesRequest.Spec) {
    companion object {
        const val PAGE_SIZE = 20
    }
}