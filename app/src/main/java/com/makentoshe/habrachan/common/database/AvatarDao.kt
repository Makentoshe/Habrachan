package com.makentoshe.habrachan.common.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.File

class AvatarDao(context: Context) {

    private val avatarsDirectory = File(context.cacheDir, "avatars").also { it.mkdirs() }

    fun insert(key: String, bitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        File(avatarsDirectory, key).writeBytes(outputStream.toByteArray())
    }

    fun get(key: String): Bitmap? {
        val file = File(avatarsDirectory, key)
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

}