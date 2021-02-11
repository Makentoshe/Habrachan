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

class ContentFilesystemQ(private val context: Context, private val scope: CoroutineScope) : ContentFilesystem(context) {

    override val permissions = listOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun permissionGranted(imageResponse: ImageResponse) {
        scope.launch(Dispatchers.IO) {
            // Define constants
            val contentFile = File(imageResponse.request.imageUrl)
            val applicationName = context.getString(R.string.app_name)
            val relativePath = "${Environment.DIRECTORY_PICTURES}${File.separator}$applicationName"

            // Create row values
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, contentFile.name)
                put(MediaStore.Images.Media.TITLE, contentFile.name)
                put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
                put(MediaStore.Images.Media.DATA, relativePath)
            }

            val contentResolver = context.contentResolver

            // Retrieve URI
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if(uri == null) {
                launch(Dispatchers.Main) {
                    Toast.makeText(context, R.string.content_error_uri, Toast.LENGTH_LONG).show()
                }
                return@launch
            }

            // Write bytes
            contentResolver.openOutputStream(uri)?.use { stream ->
                stream.write(imageResponse.bytes)
            } ?: launch(Dispatchers.Main) {
                Toast.makeText(context, R.string.content_error_stream, Toast.LENGTH_LONG).show()
            }

            // Finish report
            launch(Dispatchers.Main) {
                Toast.makeText(context, R.string.content_loading_finish, Toast.LENGTH_LONG).show()
            }
        }
    }
}