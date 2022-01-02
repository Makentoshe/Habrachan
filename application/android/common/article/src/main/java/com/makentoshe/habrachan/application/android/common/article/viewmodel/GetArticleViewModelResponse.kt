package com.makentoshe.habrachan.application.android.common.article.viewmodel

import com.makentoshe.habrachan.application.common.arena.article.get.ArticleFromArena

data class GetArticleViewModelResponse(val request: GetArticleViewModelRequest, val article: ArticleFromArena)
