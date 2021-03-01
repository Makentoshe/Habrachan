package com.makentoshe.habrachan.application.android.filesystem

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.makentoshe.habrachan.application.android.R
import java.io.File

class PicturesFileSystemQ(private val context: Context): FileSystem {

    override fun push(path: String, bytes: ByteArray) {
        val file = File(path)
        val relativePath = "${Environment.DIRECTORY_PICTURES}${File.separator}${path}"

        // Create row values
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Images.Media.TITLE, file.name)
            put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
            put(MediaStore.Images.Media.DATA, relativePath)
        }

        val contentResolver = context.contentResolver

        // Retrieve URI
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if(uri == null) throw FileSystemException(file, null, context.getString(R.string.content_error_uri))

        // Write bytes
        contentResolver.openOutputStream(uri)?.use { stream ->
            stream.write(bytes)
        } ?: throw FileSystemException(file, null, context.getString(R.string.content_error_stream))
    }
}