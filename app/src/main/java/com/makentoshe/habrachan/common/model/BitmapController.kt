package com.makentoshe.habrachan.common.model

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

class BitmapController(private val bitmap: Bitmap) {

    fun toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }
}
