package com.makentoshe.habrachan.application.android.common.arena

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.record.ContentRecord
import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.GetContentRequest
import com.makentoshe.habrachan.network.GetContentResponse
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

class ContentArenaCache @Inject constructor(
    private val database: AndroidCacheDatabase, private val cacheRoot: File
) : ArenaCache<GetContentRequest, GetContentResponse> {

    companion object : Analytics(LogAnalytic())

    private val directory = "content"
    private val limit = 200
    private val clearStep = 50

    override fun fetch(key: GetContentRequest): Result<GetContentResponse> = try {
        val cacheDirectory = File(cacheRoot, directory)
        val file = File(cacheDirectory, filename(key))

        if (!file.exists()) {
            throw FileNotFoundException(file.relativeTo(cacheDirectory).path)
        } else {
            capture(analyticEvent { "Fetch content from path ${file.relativeTo(cacheDirectory).path}" })
            Result.success(GetContentResponse(key, file.readBytes()))
        }
    } catch (exception: Exception) {
        Result.failure(ArenaStorageException("Couldn't receive a record by key: $key").initCause(exception))
    }

    override fun carry(key: GetContentRequest, value: GetContentResponse) {
        if (database.contentDao().count() > limit) {
            capture(analyticEvent { "Removing oldest $clearStep elements from cache" })
            for (record in database.contentDao().last(clearStep)) {
                database.contentDao().delete(record)
                File(cacheRoot, record.path).delete()
            }
        }

        val contentFile = fileSystemCarry(key, value)
        database.contentDao().insert(ContentRecord(key.url, contentFile.relativeTo(cacheRoot).path))
        capture(analyticEvent { "Store content with path ${contentFile.relativeTo(cacheRoot).path}" })
    }

    private fun fileSystemCarry(key: GetContentRequest, value: GetContentResponse): File {
        val cacheDirectory = File(cacheRoot, directory)
        return File(cacheDirectory, filename(key)).apply {
            if (!cacheDirectory.exists()) cacheDirectory.mkdirs()
            writeBytes(value.bytes)
        }
    }

    private fun filename(key: GetContentRequest): String = File(key.url).name
}
