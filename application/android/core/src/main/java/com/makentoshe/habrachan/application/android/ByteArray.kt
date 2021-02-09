package com.makentoshe.habrachan.application.android

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Movie
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import java.nio.ByteBuffer

/** Converts [ByteArray] to [Bitmap] using [BitmapFactory] */
fun ByteArray.toBitmap(): Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    ImageDecoder.decodeBitmap(ImageDecoder.createSource(ByteBuffer.wrap(this)))
} else {
    BitmapFactory.decodeByteArray(this, 0, size)
}

fun ByteArray.toRoundedDrawable(resources: Resources, px: Float): RoundedBitmapDrawable {
    val bitmap = RoundedBitmapDrawableFactory.create(resources, toBitmap())
    bitmap.cornerRadius = px
    return bitmap
}

@RequiresApi(Build.VERSION_CODES.P)
fun ByteArray.toAnimatedDrawable(): Drawable {
    return ImageDecoder.decodeDrawable(ImageDecoder.createSource(ByteBuffer.wrap(this)))
}

fun ByteArray.toMovie(): Movie? = Movie.decodeByteArray(this, 0, size)