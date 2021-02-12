package com.makentoshe.habrachan.application.android.filesystem

import android.content.Context
import android.os.Build

interface FileSystem {

    companion object {
        fun pictures(context: Context) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            PicturesFileSystemQ(context)
        } else {
            PicturesFileSystem(context)
        }
    }

    fun push(path: String, bytes: ByteArray)
}
