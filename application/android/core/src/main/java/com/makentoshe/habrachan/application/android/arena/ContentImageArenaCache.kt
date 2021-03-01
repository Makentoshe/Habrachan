package com.makentoshe.habrachan.application.android.arena

import android.util.Log
import com.makentoshe.habrachan.application.android.database.dao.ContentDao
import com.makentoshe.habrachan.application.android.database.record.ContentRecord
import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.network.request.ImageRequest
import com.makentoshe.habrachan.network.response.ImageResponse
import java.io.File
import java.io.FileNotFoundException

class ContentImageArenaCache(
    private val contentDao: ContentDao, private val cacheRoot: File
) : ArenaCache<ImageRequest, ImageResponse> {

    companion object {
        private inline fun capture(level: Int, message: () -> String) {
            Log.println(level, "ContentImageArenaCache", message())
        }
    }

    private val directory = "content"
    private val limit = 200
    private val clearStep = 50

    override fun fetch(key: ImageRequest): Result<ImageResponse> = try {
        val cacheDirectory = File(cacheRoot, directory)
        val file = File(cacheDirectory, filename(key))

        if (!file.exists()) {
            throw FileNotFoundException(file.relativeTo(cacheDirectory).path)
        } else {
            capture(Log.INFO) { "Fetch avatar from path ${file.relativeTo(cacheDirectory).path}" }
            Result.success(ImageResponse(key, file.readBytes()))
        }
    } catch (exception: Exception) {
        Result.failure(ArenaStorageException("Couldn't receive a record by key: $key").initCause(exception))
    }

    override fun carry(key: ImageRequest, value: ImageResponse) {
        if (contentDao.count() > limit) {
            capture(Log.INFO) { "Removing oldest $clearStep elements from cache" }
            contentDao.last(clearStep).forEach { record ->
                contentDao.delete(record)
                File(cacheRoot, record.path).delete()
            }
        }

        val contentFile = fileSystemCarry(key, value)
        contentDao.insert(ContentRecord(key.imageUrl, contentFile.relativeTo(cacheRoot).path))
        capture(Log.INFO) { "Store avatar with path ${contentFile.relativeTo(cacheRoot).path}" }
    }

    private fun fileSystemCarry(key: ImageRequest, value: ImageResponse): File {
        val cacheDirectory = File(cacheRoot, directory)
        return File(cacheDirectory, filename(key)).apply {
            if (!cacheDirectory.exists()) cacheDirectory.mkdirs()
            writeBytes(value.bytes)
        }
    }

    private fun filename(key: ImageRequest): String = File(key.imageUrl).name
}

