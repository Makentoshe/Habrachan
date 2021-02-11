package com.makentoshe.habrachan.application.android.screen.content.model

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.network.response.ImageResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Suppress("DEPRECATION")
class ContentFilesystemDefault(
    private val context: Context, private val scope: CoroutineScope
) : ContentFilesystem(context) {

    override val permissions = listOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    /** Defines constants for storing content file in the filesystem */
    override fun permissionGranted(imageResponse: ImageResponse) {
        val applicationTitle = context.getString(R.string.app_name)
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(File(directory, applicationTitle), File(imageResponse.request.imageUrl).name)
        writeFile(file, imageResponse)
    }

    /** Create file and write bytes in it */
    private fun writeFile(file: File, imageResponse: ImageResponse) = scope.launch(Dispatchers.IO) {
        if (file.parentFile?.exists() != true && file.parentFile?.mkdirs() != true) {
            launch(Dispatchers.Main) {
                Toast.makeText(context, R.string.content_error_file_parent, Toast.LENGTH_LONG).show()
            }
            return@launch
        } else if (file.createNewFile()) {
            file.writeBytes(imageResponse.bytes)
            markFile(file, imageResponse)
            launch(Dispatchers.Main) {
                Toast.makeText(context, R.string.content_loading_finish, Toast.LENGTH_LONG).show()
            }
        } else {
            // TODO replace by snackbar with replace action
            launch(Dispatchers.Main) {
                Toast.makeText(context, R.string.content_error_file_exists, Toast.LENGTH_LONG).show()
            }
        }
    }


    /** Mark file to be visible through other gallery apps */
    private fun markFile(file: File, response: ImageResponse) {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, file.name)
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/${file.extension}")
        contentValues.put(MediaStore.Images.Media.SIZE, response.bytes.size)
        contentValues.put(MediaStore.Images.Media.DATA, file.path)

        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }
}