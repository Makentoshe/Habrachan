package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId

interface GetArticleRequest2: Request {
    val articleId: ArticleId
}
