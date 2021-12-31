package com.makentoshe.habrachan.application.common.arena.article.get

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.entity.article.component.ArticleId

data class GetArticleArenaRequest(val article: ArticleId, val parameters: AdditionalRequestParameters)