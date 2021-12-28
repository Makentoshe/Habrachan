package com.makentoshe.habrachan.application.android.common.avatar.arena

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.cache.record.AvatarRecord
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.application.common.arena.EmptyArenaStorageException
import com.makentoshe.habrachan.application.common.arena.content.ContentFromArena
import com.makentoshe.habrachan.application.common.arena.content.GetContentArenaRequest
import com.makentoshe.habrachan.application.common.arena.content.GetContentArenaResponse
import com.makentoshe.habrachan.functional.Either2
import java.io.File
import javax.inject.Inject

@Suppress("SameParameterValue")
class GetAvatarArenaStorage @Inject constructor(
    private val database: AndroidCacheDatabase,
    private val cacheRoot: File,
) : ArenaCache3<GetContentArenaRequest, GetContentArenaResponse> {

    private val directory = "avatar"
    private val limit = 500
    private val clearStep = 50

    private val cacheDirectory = File(cacheRoot, directory)

    override fun fetch(key: GetContentArenaRequest): Either2<GetContentArenaResponse, ArenaStorageException>  = try {
        val file = File(cacheDirectory, File(key.contentUrl).name)

        if (file.exists()) {
            capture(analyticEvent { "Fetch avatar from path ${file.relativeTo(cacheDirectory).path}" })
            Either2.Left(GetContentArenaResponse(key, ContentFromArena(file.readBytes())))
        } else {
            Either2.Right(EmptyArenaStorageException())
        }
    } catch (exception: Exception) {
        Either2.Right(ArenaStorageException(exception))
    }


    override fun carry(key: GetContentArenaRequest, value: GetContentArenaResponse) {
        if (database.avatarDao().count() > limit) clearOldestCacheElements(clearStep)

        if (cacheDirectory.exists().not() && cacheDirectory.mkdirs().not()) {
            return capture(analyticEvent { "Issue with creating an avatar cache directory" })
        }

        File(cacheDirectory, File(key.contentUrl).name).also {
            database.avatarDao().insert(AvatarRecord(it.relativeTo(cacheRoot).path, key.contentUrl))
            capture(analyticEvent { "Store avatar with path ${it.relativeTo(cacheRoot).path}" })
        }.writeBytes(value.content.bytes)
    }

    private fun clearOldestCacheElements(count: Int) {
        capture(analyticEvent { "Removing oldest $count elements from cache" })
        for (record in database.avatarDao().last(count)) {
            database.avatarDao().delete(record)
            File(cacheRoot, record.location).delete()
        }
    }

    companion object : Analytics(LogAnalytic())
}