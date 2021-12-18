package com.makentoshe.application.android.common.user.get.arena

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.application.common.arena.EmptyArenaStorageException
import com.makentoshe.habrachan.application.common.arena.user.get.GetUserArenaRequest
import com.makentoshe.habrachan.application.common.arena.user.get.GetUserArenaResponse
import com.makentoshe.habrachan.functional.Either2
import javax.inject.Inject

class GetUserArenaStorage @Inject constructor(
    private val database: AndroidCacheDatabase,
) : ArenaCache3<GetUserArenaRequest, GetUserArenaResponse> {

    override fun fetch(key: GetUserArenaRequest): Either2<GetUserArenaResponse, ArenaStorageException> {
        return Either2.Right(EmptyArenaStorageException())
    }

    override fun carry(key: GetUserArenaRequest, value: GetUserArenaResponse) {
    }

    companion object : Analytics(LogAnalytic())

}