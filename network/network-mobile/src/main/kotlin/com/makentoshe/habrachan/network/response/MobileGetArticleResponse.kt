package com.makentoshe.habrachan.network.response

import com.makentoshe.habrachan.entity.mobiles.Article
import com.makentoshe.habrachan.network.request.MobileGetArticleRequest

data class MobileGetArticleResponse(
    override val request: MobileGetArticleRequest,
    override val article: Article
): GetArticleResponse2