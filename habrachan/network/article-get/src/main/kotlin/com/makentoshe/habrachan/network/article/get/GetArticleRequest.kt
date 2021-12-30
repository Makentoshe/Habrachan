package com.makentoshe.habrachan.network.article.get

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.entity.article.component.ArticleId

data class GetArticleRequest(val articleId: ArticleId, val parameters: AdditionalRequestParameters)