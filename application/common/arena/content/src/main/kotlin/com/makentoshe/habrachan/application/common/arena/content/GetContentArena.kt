package com.makentoshe.habrachan.application.common.arena.content

import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.FlowArena
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.content.get.GetContentManager
import com.makentoshe.habrachan.network.content.get.GetContentRequest
import javax.inject.Inject

class GetContentArena @Inject constructor(
    private val manager: GetContentManager,
    override val arenaStorage: ArenaCache3<in GetContentArenaRequest, GetContentArenaResponse>,
) : FlowArena<GetContentArenaRequest, GetContentArenaResponse>() {
    override suspend fun suspendSourceFetch(key: GetContentArenaRequest): Either2<GetContentArenaResponse, Throwable> {
        val managerRequest = GetContentRequest(key.contentUrl, key.parameters)
        val managerResponse = manager.execute(managerRequest)
        return managerResponse.mapLeft { GetContentArenaResponse(key, ContentFromArena(it.content.bytes)) }
    }
}