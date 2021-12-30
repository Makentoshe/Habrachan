package com.makentoshe.habrachan.network.article.get

import com.makentoshe.habrachan.entity.article.Article

data class GetArticleResponse(val request: GetArticleRequest, val article: Article)