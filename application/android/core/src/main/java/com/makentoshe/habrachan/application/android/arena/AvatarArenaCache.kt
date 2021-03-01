package com.makentoshe.habrachan.application.android.arena

import android.util.Log
import com.makentoshe.habrachan.application.android.database.AvatarDao
import com.makentoshe.habrachan.application.android.database.record.AvatarRecord
import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.network.request.ImageRequest
import com.makentoshe.habrachan.network.response.ImageResponse
import java.io.File
import java.io.FileNotFoundException

// TODO think about alternative caching variant
// Avatar url can be https://hsto.org/getpro/habr/avatars/190/64d/c63/19064dc635c410289299504131316a76.jpg
// So it is a good point to get /avatars/190/64d/c63/19064dc635c410289299504131316a76.jpg as location path
// But https://hsto.org/getpro/habr/ may change in future and we couldn't hardcode it
class AvatarArenaCache(
    private val avatarDao: AvatarDao, private val cacheRoot: File
) : ArenaCache<ImageRequest, ImageResponse> {

    companion object {
        private inline fun capture(level: Int, message: () -> String) {
            Log.println(level, "AvatarArenaCache", message())
        }
    }

    private val directory = "avatar"
    private val limit = 500
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
        if (avatarDao.count() > limit) {
            capture(Log.INFO) { "Removing oldest $clearStep elements from cache" }
            avatarDao.last(clearStep).forEach { record ->
                avatarDao.delete(record)
                File(cacheRoot, record.location).delete()
            }
        }

        val avatarFile = fileSystemCarry(key, value)
        avatarDao.insert(AvatarRecord(avatarFile.relativeTo(cacheRoot).path, key.imageUrl))
        capture(Log.INFO) { "Store avatar with path ${avatarFile.relativeTo(cacheRoot).path}" }
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
