package com.makentoshe.habrachan.model.article.comment

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap

class BitmapController(private val bitmap: Bitmap) {

    fun roundCornersPx(context: Context, radius: Int): Bitmap {
        val roundedDrawable = RoundedBitmapDrawableFactory.create(context.resources, bitmap)
        roundedDrawable.cornerRadius = radius.toFloat()
        return roundedDrawable.toBitmap()
    }
}