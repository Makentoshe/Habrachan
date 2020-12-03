package com.makentoshe.habrachan.application.android.arena

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.response.ArticlesResponse

class ArticlesArenaCache : ArenaCache<GetArticlesRequest, ArticlesResponse> {

    override fun fetch(key: GetArticlesRequest): Result<ArticlesResponse> {
        return Result.failure(ArenaStorageException("ArticlesArenaCache"))
    }

    override fun carry(key: GetArticlesRequest, value: ArticlesResponse) {

    }
}
