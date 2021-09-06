package com.makentoshe.habrachan.application.android.filesystem

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import java.io.File

class PicturesFileSystem(private val context: Context) : FileSystem {

    override fun push(path: String, bytes: ByteArray) {
        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(root, path)

        if (file.parentFile?.exists() != true && file.parentFile?.mkdirs() != true) {
            throw FileSystemException(file.parentFile ?: file, null, context.getString(R.string.content_error_file_parent))
        } else if (file.createNewFile()) {
            file.writeBytes(bytes)
            markFile(file, bytes)
        } else {
            throw FileAlreadyExistsException(file, null, context.getString(R.string.content_error_file_exists))
        }
    }

    /** Mark file to be visible through other gallery apps */
    private fun markFile(file: File, bytes: ByteArray) {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, file.name)
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/${file.extension}")
        contentValues.put(MediaStore.Images.Media.SIZE, bytes.size)
        contentValues.put(MediaStore.Images.Media.DATA, file.path)

        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }
}