package com.makentoshe.habrachan.network.response

import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.network.request.GetArticleRequest2

interface GetArticleResponse2 {
    val request: GetArticleRequest2
    val article: Article
}