package com.makentoshe.habrachan.common.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.File

class AvatarDao(context: Context) {

    private val avatarsDirectory = File(context.cacheDir, "avatars").also { it.mkdirs() }

    fun insert(key: String, bitmap: Bitmap) {
        val normalizedKey = normalizeKey(key)
        val outputStream = ByteArrayOutputStream().also {
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, it)
        }
        val file = File(avatarsDirectory, normalizedKey)
        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeBytes(outputStream.toByteArray())
    }

    fun get(key: String): Bitmap? {
        val normalizedKey = normalizeKey(key)
        val file = File(avatarsDirectory, normalizedKey)
        if (!file.exists()) return null
        val bytes = file.readBytes()
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun getAll(): List<File> {
        return avatarsDirectory.listFiles().toList()
    }

    fun clear() {
        avatarsDirectory.deleteRecursively()
        avatarsDirectory.mkdirs()
    }

    private fun normalizeKey(key: String): String {
        val filepath = key.split("avatars")
        return if (filepath.size > 1) filepath[1] else File(key).name
    }

}