package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId

data class GetArticleMobileRequest(override val articleId: ArticleId): MobileRequest(), GetArticleRequest2