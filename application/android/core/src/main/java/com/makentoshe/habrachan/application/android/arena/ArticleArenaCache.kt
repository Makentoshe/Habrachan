package com.makentoshe.habrachan.application.android.arena

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.network.request.GetArticleRequest
import com.makentoshe.habrachan.network.response.ArticleResponse

class ArticleArenaCache: ArenaCache<GetArticleRequest, ArticleResponse> {

    override fun fetch(key: GetArticleRequest): Result<ArticleResponse> {
        return Result.failure(ArenaStorageException("ArticleArenaCache"))
    }

    override fun carry(key: GetArticleRequest, value: ArticleResponse) {

    }
}