package com.makentoshe.habrachan.application.common.arena.article.get

import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.FlowArena
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.article.get.GetArticleManager
import com.makentoshe.habrachan.network.article.get.GetArticleRequest
import javax.inject.Inject

class GetArticleArena @Inject constructor(
    private val manager: GetArticleManager,
    override val arenaStorage: ArenaCache3<in GetArticleArenaRequest, GetArticleArenaResponse>,
) : FlowArena<GetArticleArenaRequest, GetArticleArenaResponse>() {

    override suspend fun suspendSourceFetch(key: GetArticleArenaRequest): Either2<GetArticleArenaResponse, Throwable> {
        val managerRequest = GetArticleRequest(key.article, key.parameters)
        val managerResponse = manager.execute(managerRequest)
        return managerResponse.mapLeft { GetArticleArenaResponse(key, ArticleFromArena(it.article.parameters)) }
    }

}